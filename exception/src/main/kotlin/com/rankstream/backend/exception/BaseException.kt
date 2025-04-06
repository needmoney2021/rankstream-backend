package com.rankstream.backend.exception

import com.rankstream.backend.exception.dto.response.ApiError
import com.rankstream.backend.exception.enums.ErrorCode
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

abstract class BaseException(
    status: HttpStatus,
    override val message: String,
    val errorCode: ErrorCode,
) : ResponseStatusException(status, message) {

    fun getApiError(): ApiError {
        return ApiError(this.errorCode)
    }

}


class NotFoundException(
    message: String,
    errorCode: ErrorCode,
    val targetId: Any? = null
) : BaseException(HttpStatus.NOT_FOUND, message, errorCode)

open class BadRequestException(
    message: String,
    errorCode: ErrorCode,
) : BaseException(HttpStatus.BAD_REQUEST, message, errorCode)

class DuplicatedException(
    message: String
) : BadRequestException(message, ErrorCode.DUPLICATED)

class UnauthorizedException(
    message: String,
    errorCode: ErrorCode,
) : BaseException(HttpStatus.UNAUTHORIZED, message, errorCode)

class ForbiddenException(
    message: String,
    errorCode: ErrorCode,
) : BaseException(HttpStatus.FORBIDDEN, message, errorCode)

