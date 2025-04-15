package com.rankstream.backend.exception.enums

enum class ErrorCode(
    val message: String
) {
    // Common Errors
    UNKNOWN("알 수 없는 오류가 발생했습니다."),
    INVALID_INPUT("잘못된 입력입니다."),
    NOT_FOUND("요청한 리소스를 찾을 수 없습니다."),
    UNAUTHORIZED("인증이 필요합니다."),
    FORBIDDEN("접근 권한이 없습니다."),
    DUPLICATED("중복된 데이터가 존재합니다."),
    METHOD_NOT_ALLOWED("허용되지 않은 메소드입니다."),
    UNSUPPORTED_MEDIA_TYPE("지원하지 않는 미디어 타입입니다."),

    // Token Errors
    TOKEN_EXPIRED("토큰이 만료되었습니다."),
    AUTHENTICATION_FAILED("인증에 실패하였습니다."),

    // Member Errors
    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다."),
    WRONG_PASSWORD("비밀번호가 잘못되었습니다."),
    MEMBER_ALREADY_EXISTS("이미 존재하는 회원입니다."),
    INVALID_MEMBER_ID("잘못된 회원 ID입니다."),

    // Company Errors
    COMPANY_NOT_FOUND("회사를 찾을 수 없습니다."),
    COMPANY_ALREADY_EXISTS("이미 존재하는 회사입니다."),
    INVALID_BUSINESS_LICENSE("잘못된 사업자등록번호입니다."),

    // Transaction Errors
    TRANSACTION_NOT_FOUND("거래를 찾을 수 없습니다."),
    INVALID_TRANSACTION_AMOUNT("잘못된 거래 금액입니다."),
    TRANSACTION_ALREADY_REFUNDED("이미 환불된 거래입니다."),

    // Schedule Errors
    SCHEDULE_NOT_FOUND("스케줄을 찾을 수 없습니다."),
    INVALID_CRON_EXPRESSION("잘못된 cron 표현식입니다.")
}
