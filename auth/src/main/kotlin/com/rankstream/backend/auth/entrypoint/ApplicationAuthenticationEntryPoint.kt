package com.rankstream.backend.auth.entrypoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.rankstream.backend.exception.enums.ErrorCode
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class ApplicationAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"

        val error = mapOf(
            "code" to ErrorCode.UNAUTHORIZED.name,
            "message" to ErrorCode.UNAUTHORIZED.message
        )

        objectMapper.writeValue(response.outputStream, error)
    }
}
