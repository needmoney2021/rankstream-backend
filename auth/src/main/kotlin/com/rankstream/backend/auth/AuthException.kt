package com.rankstream.backend.auth

import com.rankstream.backend.exception.enums.ErrorCode
import org.springframework.security.core.AuthenticationException

class AuthenticationExpiredException(
    private val errorCode: ErrorCode = ErrorCode.TOKEN_EXPIRED,
    throwable: Throwable? = null
) : AuthenticationException(errorCode.message, throwable)

class AuthenticationInvalidException(
    private val errorCode: ErrorCode = ErrorCode.AUTHENTICATION_FAILED,
    throwable: Throwable? = null
) : AuthenticationException(errorCode.message, throwable)
