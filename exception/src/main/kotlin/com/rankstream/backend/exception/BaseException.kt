package com.rankstream.backend.exception

import com.rankstream.backend.exception.dto.response.ApiError
import com.rankstream.backend.exception.enums.ErrorCode
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.server.ResponseStatusException

abstract class BaseException(
    status: HttpStatus,
    override val message: String,
    val errorCode: ErrorCode,
) : ResponseStatusException(status, message) {

    fun getApiError(): ApiError {
        return ApiError.of(errorCode)
    }

}

@ResponseStatus(HttpStatus.NOT_FOUND)
class NotFoundException(
    message: String,
    errorCode: ErrorCode,
    val targetId: Any? = null
) : BaseException(HttpStatus.NOT_FOUND, message, errorCode)

@ResponseStatus(HttpStatus.BAD_REQUEST)
open class BadRequestException(
    message: String,
    errorCode: ErrorCode,
) : BaseException(HttpStatus.BAD_REQUEST, message, errorCode)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class DuplicatedException(
    message: String
) : BadRequestException(message, ErrorCode.DUPLICATED)

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class UnauthorizedException(
    message: String,
    errorCode: ErrorCode,
) : BaseException(HttpStatus.UNAUTHORIZED, message, errorCode)

@ResponseStatus(HttpStatus.FORBIDDEN)
class ForbiddenException(
    message: String,
    errorCode: ErrorCode,
) : BaseException(HttpStatus.FORBIDDEN, message, errorCode)

