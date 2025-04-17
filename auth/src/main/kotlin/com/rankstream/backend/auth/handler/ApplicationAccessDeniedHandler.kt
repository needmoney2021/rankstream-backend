package com.rankstream.backend.auth.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.rankstream.backend.exception.enums.ErrorCode
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class ApplicationAccessDeniedHandler(
    private val objectMapper: ObjectMapper
) : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        response.status = HttpStatus.FORBIDDEN.value()
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"

        val error = mapOf(
            "code" to ErrorCode.FORBIDDEN.name,
            "message" to ErrorCode.FORBIDDEN.message
        )

        objectMapper.writeValue(response.outputStream, error)
    }
}
