package com.rankstream.backend.domain.auditor

import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.domain.AuditorAware
import org.springframework.stereotype.Component
import java.util.*

@Component
class EntityAuditor(
    private val httpServletRequest: HttpServletRequest,
) : AuditorAware<String> {

    override fun getCurrentAuditor(): Optional<String> {
        return Optional.ofNullable(this.httpServletRequest.getAttribute("user-id") as String)
    }
}
