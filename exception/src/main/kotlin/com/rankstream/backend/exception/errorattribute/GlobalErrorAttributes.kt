package com.rankstream.backend.exception.errorattribute

import com.rankstream.backend.exception.BadRequestException
import com.rankstream.backend.exception.UnauthorizedException
import com.rankstream.backend.exception.enums.ErrorCode
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.context.request.WebRequest

@Component
class GlobalErrorAttributes : DefaultErrorAttributes() {

    override fun getErrorAttributes(
        webRequest: WebRequest?,
        options: ErrorAttributeOptions?
    ): Map<String?, Any?>? {
        val errorAttributes = super.getErrorAttributes(webRequest, options)
        val throwable = getError(webRequest)

        if (throwable is UnauthorizedException) {
            errorAttributes["status"] = 401
            errorAttributes["error"] = HttpStatus.UNAUTHORIZED.reasonPhrase
            errorAttributes["message"] = ErrorCode.AUTHENTICATION_FAILED.message
            errorAttributes["code"] = ErrorCode.AUTHENTICATION_FAILED.name
        } else if (throwable is BadRequestException) {
            val errorCode = throwable.errorCode
            errorAttributes["status"] = 400
            errorAttributes["error"] = HttpStatus.UNAUTHORIZED.reasonPhrase
            errorAttributes["message"] = errorCode.message
            errorAttributes["code"] = errorCode.name
        } else {
            errorAttributes["status"] = 500
            errorAttributes["error"] = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase
            errorAttributes["message"] = ErrorCode.UNKNOWN.message
            errorAttributes["code"] = ErrorCode.UNKNOWN.name
        }

        options?.isIncluded(ErrorAttributeOptions.Include.STACK_TRACE)?.let {
            if (!it) {
                errorAttributes.remove("stack_trace")
            }
        }

        options?.isIncluded(ErrorAttributeOptions.Include.EXCEPTION)?.let {
            if (!it) {
                errorAttributes.remove("exception")
            }
        }

        return errorAttributes
    }
}
