package com.rankstream.backend.exception.enums

enum class ErrorCode(
    val message: String
) {

    // 400
    DUPLICATED("Your request attempts to create duplicate data."),

    INVALID_REQUEST("Invalid input detected. Please check your input again."),

    // 401
    AUTHORIZATION_MISSING("Missing authentication credentials."),

    // 403
    FORBIDDEN("Access denied."),

    // 404
    NOT_FOUND("Data requested was not found."),

    // 405
    METHOD_NOT_ALLOWED("Request method not allowed."),

    // 415
    UNSUPPORTED_MEDIA_TYPE("Unsupported media type."),


}
