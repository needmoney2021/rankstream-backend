package com.rankstream.backend.domain.auditor

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.data.domain.AuditorAware
import org.springframework.stereotype.Component
import java.util.*

@Component
class EntityAuditor(
    private val httpServletRequest: HttpServletRequest,
    private val environment: Environment
) : AuditorAware<String> {

    private val log = LoggerFactory.getLogger(EntityAuditor::class.java)

    override fun getCurrentAuditor(): Optional<String> {
        return try {
            val userName = httpServletRequest.getAttribute("user-name") as? String
            Optional.of(userName ?: resolveSystemAuditor())
        } catch (e: Exception) {
            log.warn("Failed to resolve current auditor: ${e.message}")
            Optional.of(resolveSystemAuditor())
        }
    }

    private fun resolveSystemAuditor(): String {
        val activeProfiles = environment.activeProfiles.toSet()

        return when {
            "test" in activeProfiles -> "TEST"
            "batch" in activeProfiles -> "BATCH"
            else -> "SYSTEM"
        }
    }
}
