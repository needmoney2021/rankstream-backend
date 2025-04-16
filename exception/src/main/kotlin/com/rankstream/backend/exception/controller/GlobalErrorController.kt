package com.rankstream.backend.exception.controller

import com.rankstream.backend.exception.dto.response.ApiError
import com.rankstream.backend.exception.enums.ErrorCode
import jakarta.servlet.RequestDispatcher
import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class GlobalErrorController : ErrorController {

    @RequestMapping("/error")
    fun handleError(request: HttpServletRequest): ResponseEntity<ApiError> {
        val statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE) as? Int
            ?: HttpStatus.INTERNAL_SERVER_ERROR.value()

        val errorCode = when (statusCode) {
            HttpStatus.NOT_FOUND.value() -> ErrorCode.NOT_FOUND
            HttpStatus.METHOD_NOT_ALLOWED.value() -> ErrorCode.METHOD_NOT_ALLOWED
            HttpStatus.UNSUPPORTED_MEDIA_TYPE.value() -> ErrorCode.UNSUPPORTED_MEDIA_TYPE
            HttpStatus.UNAUTHORIZED.value() -> ErrorCode.UNAUTHORIZED
            else -> ErrorCode.UNKNOWN
        }

        return ResponseEntity
            .status(statusCode)
            .body(ApiError.of(errorCode))
    }
}
