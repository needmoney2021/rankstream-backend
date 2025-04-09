package com.rankstream.backend.exception.handler

import com.rankstream.backend.exception.BaseException
import com.rankstream.backend.exception.dto.response.ApiError
import com.rankstream.backend.exception.enums.ErrorCode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.NoHandlerFoundException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(e: BaseException): ResponseEntity<ApiError> {
        return ResponseEntity
            .status(e.statusCode)
            .body(ApiError.of(e.errorCode))
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFoundException(): ResponseEntity<ApiError> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiError.of(ErrorCode.NOT_FOUND))
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException(): ResponseEntity<ApiError> {
        return ResponseEntity
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .body(ApiError.of(ErrorCode.METHOD_NOT_ALLOWED))
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    fun handleHttpMediaTypeNotSupportedException(): ResponseEntity<ApiError> {
        return ResponseEntity
            .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
            .body(ApiError.of(ErrorCode.UNSUPPORTED_MEDIA_TYPE))
    }

    @ExceptionHandler(
        MethodArgumentNotValidException::class,
        BindException::class,
        MethodArgumentTypeMismatchException::class,
        MissingServletRequestParameterException::class,
        HttpMessageNotReadableException::class
    )
    fun handleValidationException(): ResponseEntity<ApiError> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiError.of(ErrorCode.INVALID_INPUT))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(): ResponseEntity<ApiError> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiError.of(ErrorCode.UNKNOWN))
    }
} 