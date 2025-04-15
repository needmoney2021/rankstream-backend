package com.rankstream.backend.auth.config

import com.rankstream.backend.auth.filter.JwtAuthenticationFilter
import com.rankstream.backend.auth.service.JwtService
import com.rankstream.backend.auth.service.MemberDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SecurityFilterConfig {

    @Bean
    fun jwtAuthenticationFilter(jwtService: JwtService, memberDetailsService: MemberDetailsService): JwtAuthenticationFilter {
        return JwtAuthenticationFilter(jwtService, memberDetailsService)
    }
}
