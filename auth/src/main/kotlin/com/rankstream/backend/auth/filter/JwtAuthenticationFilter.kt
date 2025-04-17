package com.rankstream.backend.auth.filter

import com.rankstream.backend.auth.service.AdministratorDetailsService
import com.rankstream.backend.auth.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID

class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val administratorDetailsService: AdministratorDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        MDC.put("requestId", UUID.randomUUID().toString())
        try {
            val token = extractToken(request)

            if (token != null) {
                val decoded = jwtService.decodeToken(token)
                val memberIdx = decoded.subject

                val userDetails = administratorDetailsService.loadUserByUsername(memberIdx)
                    ?: throw UsernameNotFoundException("User not found")

                val auth = UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.authorities
                ).apply {
                    details = WebAuthenticationDetailsSource().buildDetails(request)
                }

                SecurityContextHolder.getContext().authentication = auth
                request.setAttribute("user-id", memberIdx)
            }

            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            SecurityContextHolder.clearContext()
            throw e
        } finally {
            MDC.clear()
        }
    }

    private fun extractToken(request: HttpServletRequest): String? {
        val header = request.getHeader("Authorization") ?: return null
        return if (header.startsWith("Bearer ")) header.substring(7) else null
    }
}
