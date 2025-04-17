package com.rankstream.backend.auth

import com.rankstream.backend.exception.enums.ErrorCode
import org.springframework.security.core.AuthenticationException

class AuthenticationExpiredException(
    private val errorCode: ErrorCode = ErrorCode.TOKEN_EXPIRED
) : AuthenticationException(errorCode.message)

class AuthenticationInvalidException(
    private val errorCode: ErrorCode = ErrorCode.AUTHENTICATION_FAILED
) : AuthenticationException(errorCode.message)
