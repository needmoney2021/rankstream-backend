package com.rankstream.backend.auth.filter

import com.rankstream.backend.auth.service.JwtService
import com.rankstream.backend.auth.service.AdministratorDetailsService
import com.rankstream.backend.exception.UnauthorizedException
import com.rankstream.backend.exception.enums.ErrorCode
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val administratorDetailsService: AdministratorDetailsService,
) : OncePerRequestFilter() {

    companion object {
        private val log = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = extractToken(request)
        if (token != null) {
            try {
                val decoded = jwtService.decodeToken(token)
                val memberIdx = decoded.subject
                val userDetails = administratorDetailsService.loadUserByUsername(memberIdx)

                val auth = UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails?.authorities
                )

                SecurityContextHolder.getContext().authentication = auth
                request.setAttribute("user-id", memberIdx)
            } catch (e: Exception) {
                log.error("JWT 인증 실패: ${e.message}")
                SecurityContextHolder.clearContext()
                if (e is UnauthorizedException) {
                    throw e
                }
                throw UnauthorizedException("인증 실패.", ErrorCode.UNAUTHORIZED)
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun extractToken(request: HttpServletRequest): String? {
        val header = request.getHeader("Authorization") ?: return null
        return if (header.startsWith("Bearer ")) header.substring(7) else null
    }
}
