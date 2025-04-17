package com.rankstream.backend.exception.controller

import com.rankstream.backend.exception.UnauthorizedException
import com.rankstream.backend.exception.dto.response.ApiError
import com.rankstream.backend.exception.enums.ErrorCode
import jakarta.servlet.RequestDispatcher
import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.web.servlet.error.ErrorAttributes
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.ServletWebRequest

@RestController
class GlobalErrorController(
    private val errorAttributes: ErrorAttributes
) : ErrorController {

    @RequestMapping("/error")
    fun handleError(request: HttpServletRequest): ResponseEntity<ApiError> {
        val webRequest = ServletWebRequest(request)
        val throwable = errorAttributes.getError(webRequest)
        val status = when (throwable) {
            is UnauthorizedException -> HttpStatus.UNAUTHORIZED
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }
        val errorCode = when (status) {
            HttpStatus.UNAUTHORIZED -> ErrorCode.AUTHENTICATION_FAILED
            else -> ErrorCode.UNKNOWN
        }

        return ResponseEntity
            .status(status)
            .body(ApiError.of(errorCode))
    }
}
