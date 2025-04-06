package com.rankstream.backend.exception.dto.response

import com.rankstream.backend.exception.enums.ErrorCode

class ApiError(
    errorCode: ErrorCode,
) {

    val code: String = errorCode.name

    val message: String = errorCode.message

}
