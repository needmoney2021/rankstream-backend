package com.rankstream.backend.auth.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.rankstream.backend.auth.AuthenticationExpiredException
import com.rankstream.backend.auth.AuthenticationInvalidException
import com.rankstream.backend.auth.service.AdministratorDetailsService
import com.rankstream.backend.auth.service.JwtService
import com.rankstream.backend.exception.dto.response.ApiError
import com.rankstream.backend.exception.enums.ErrorCode
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.bind.MissingRequestCookieException
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val administratorDetailsService: AdministratorDetailsService,
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        MDC.put("requestId", UUID.randomUUID().toString())
        try {
            val token = extractToken(request)

            if (token != null) {
                try {
                    val decoded = jwtService.decodeToken(token)
                    val memberIdx = decoded.subject

                    val userDetails = administratorDetailsService.loadUserByUsername(memberIdx)
                        ?: throw UsernameNotFoundException("User not found")

                    val auth = UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.authorities
                    ).apply {
                        details = WebAuthenticationDetailsSource().buildDetails(request)
                    }

                    SecurityContextHolder.getContext().authentication = auth
                    request.setAttribute("user-id", memberIdx)
                    request.setAttribute("user-name", userDetails.username)
                } catch (e: Exception) {
                    SecurityContextHolder.clearContext()
                    lateinit var errorCode: ErrorCode
                    lateinit var httpStatus: HttpStatus
                    if (e is AuthenticationExpiredException || e is MissingRequestCookieException) {
                        errorCode = ErrorCode.TOKEN_EXPIRED
                        httpStatus = HttpStatus.UNAUTHORIZED
                    } else if (e is AuthenticationInvalidException) {
                        errorCode = ErrorCode.AUTHENTICATION_FAILED
                        httpStatus = HttpStatus.UNAUTHORIZED
                    } else {
                        errorCode = ErrorCode.UNKNOWN
                        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
                    }
                    val apiError = ApiError.of(errorCode)
                    response.status = httpStatus.value()
                    response.contentType = "application/json"
                    response.characterEncoding = "UTF-8"
                    response.writer.write(objectMapper.writeValueAsString(apiError))
                    return
                }
            }

            filterChain.doFilter(request, response)
        } finally {
            MDC.clear()
        }
    }

    private fun extractToken(request: HttpServletRequest): String? {
        val header = request.getHeader("Authorization") ?: return null
        return if (header.startsWith("Bearer ")) header.substring(7) else null
    }
}
