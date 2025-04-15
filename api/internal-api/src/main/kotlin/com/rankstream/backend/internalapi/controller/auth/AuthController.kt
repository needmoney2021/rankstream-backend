package com.rankstream.backend.internalapi.controller.auth

import com.rankstream.backend.auth.service.JwtService
import com.rankstream.backend.domain.auth.dto.AuthResponse
import com.rankstream.backend.domain.auth.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
class AuthController(
    private val jwtService: JwtService,
    private val authService: AuthService,
) {

    @GetMapping("/refresh-token")
    fun refreshToken(@RequestHeader("Refresh-Token") refreshToken: String): ResponseEntity<AuthResponse> {

        authService.findRefreshTokenInWhiteList(refreshToken)

        val newTokens = jwtService.generateNewTokensViaRefreshToken(refreshToken)
        authService.dropAndSaveRefreshToken(refreshToken, newTokens[1])
        return ResponseEntity.ok(AuthResponse(newTokens[0], newTokens[1]))
    }
}

