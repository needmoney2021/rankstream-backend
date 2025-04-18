package com.rankstream.backend.auth.config

import com.rankstream.backend.auth.entrypoint.ApplicationAuthenticationEntryPoint
import com.rankstream.backend.auth.filter.JwtAuthenticationFilter
import com.rankstream.backend.auth.handler.ApplicationAccessDeniedHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val applicationAuthenticationEntryPoint: ApplicationAuthenticationEntryPoint,
    private val applicationAccessDeniedHandler: ApplicationAccessDeniedHandler
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        val tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse()
        tokenRepository.cookiePath = "/"
        tokenRepository.setCookieName("XSRF-TOKEN")

        val requestHandler = CsrfTokenRequestAttributeHandler()
        requestHandler.setCsrfRequestAttributeName(CsrfToken::class.java.name)

        http
            .cors {  }
            .httpBasic { it.disable() }
            .csrf { csrf ->
                csrf
                    .csrfTokenRepository(tokenRepository)
                    .csrfTokenRequestHandler(requestHandler)
            }
            .formLogin { it.disable() }
            .logout { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it
                    .requestMatchers(HttpMethod.POST, "/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "/signup").permitAll()
                    .requestMatchers(HttpMethod.GET, "/csrf").permitAll()
                    .requestMatchers(HttpMethod.POST, "/auth/signin").permitAll()
                    .requestMatchers("/error").permitAll()
                    .requestMatchers(HttpMethod.GET, "/auth/refresh-token").permitAll()
                    .anyRequest().authenticated()
            }
            .exceptionHandling {
                it.authenticationEntryPoint(applicationAuthenticationEntryPoint)
                    .accessDeniedHandler(applicationAccessDeniedHandler)
            }

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

}
