package com.rankstream.backend.exception.dto.response

import com.rankstream.backend.exception.enums.ErrorCode

data class ApiError(
    val code: String,
    val message: String
) {
    companion object {
        fun of(errorCode: ErrorCode): ApiError {
            return ApiError(
                code = errorCode.name,
                message = errorCode.message
            )
        }
    }
}
