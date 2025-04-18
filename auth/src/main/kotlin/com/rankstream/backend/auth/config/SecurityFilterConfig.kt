package com.rankstream.backend.auth.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.rankstream.backend.auth.filter.JwtAuthenticationFilter
import com.rankstream.backend.auth.service.AdministratorDetailsService
import com.rankstream.backend.auth.service.JwtService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SecurityFilterConfig {

    @Bean
    fun jwtAuthenticationFilter(jwtService: JwtService, administratorDetailsService: AdministratorDetailsService, objectMapper: ObjectMapper): JwtAuthenticationFilter {
        return JwtAuthenticationFilter(jwtService, administratorDetailsService, objectMapper)
    }

}
