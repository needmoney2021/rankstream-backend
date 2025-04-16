package com.rankstream.backend.internalapi.controller.auth

import com.rankstream.backend.auth.service.JwtService
import com.rankstream.backend.domain.admin.dto.request.SigninRequest
import com.rankstream.backend.domain.admin.dto.response.SigninResponse
import com.rankstream.backend.domain.admin.service.AdministratorService
import com.rankstream.backend.domain.auth.dto.AuthResponse
import com.rankstream.backend.domain.auth.service.AuthService
import org.springframework.core.env.Environment
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
class AuthController(
    private val jwtService: JwtService,
    private val authService: AuthService,
    private val administratorService: AdministratorService,
    private val environment: Environment
) {

    @GetMapping("/refresh-token")
    fun refreshToken(@CookieValue("Refresh-Token") refreshToken: String): ResponseEntity<AuthResponse> {

        authService.findRefreshTokenInWhiteList(refreshToken)

        val tokenPair = jwtService.generateNewTokensViaRefreshToken(refreshToken)
        authService.replaceRefreshToken(refreshToken, tokenPair.refreshToken)
        val cookie = generateRefreshTokenCookie(tokenPair.refreshToken, JwtService.REFRESH_TOKEN_EXPIRE_SECONDS)
        return ResponseEntity
            .ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(AuthResponse(tokenPair.accessToken))
    }

    @PostMapping("/signin")
    fun signin(@RequestBody @Validated signinRequest: SigninRequest): ResponseEntity<SigninResponse> {
        val administrator = administratorService.findByUserId(signinRequest.email)
        val accessToken = jwtService.generateAccessToken(administrator, signinRequest.password)
        val refreshToken = jwtService.generateRefreshToken(administrator.idx!!)
        authService.saveRefreshToken(refreshToken)

        val cookie = generateRefreshTokenCookie(refreshToken, JwtService.REFRESH_TOKEN_EXPIRE_SECONDS)

        return ResponseEntity
            .ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(SigninResponse.fromEntityAndToken(administrator, accessToken))
    }

    @DeleteMapping("/sign-out")
    fun signOut(@CookieValue("Refresh-Token") refreshToken: String): ResponseEntity<Void> {
        authService.deleteRefreshToken(refreshToken)
        val expiredCookie = generateRefreshTokenCookie("", 0)
        return ResponseEntity
            .noContent()
            .header(HttpHeaders.SET_COOKIE, expiredCookie.toString())
            .build()
    }

    private fun generateRefreshTokenCookie(refreshToken: String, maxAge: Long): ResponseCookie {
        return ResponseCookie.from("Refresh-Token", refreshToken)
            .httpOnly(true)
            .secure(environment.activeProfiles.contains("prod"))
            .path("/")
            .sameSite("Strict")
            .maxAge(maxAge)
            .build()
    }
}

