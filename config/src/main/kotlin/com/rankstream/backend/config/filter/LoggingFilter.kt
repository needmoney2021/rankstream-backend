package com.rankstream.backend.config.filter

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.nio.charset.Charset
import java.util.UUID

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class LoggingFilter(
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

    companion object {
        private val log = LoggerFactory.getLogger(LoggingFilter::class.java)
        private const val SLOW_REQUEST_THRESHOLD_MS = 1000L
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestId = UUID.randomUUID().toString()
        MDC.put("requestId", requestId)

        val startTime = System.currentTimeMillis()
        val wrappedRequest = ContentCachingRequestWrapper(request)
        val wrappedResponse = ContentCachingResponseWrapper(response)

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse)
        } catch (e: Exception) {
            log.error("Exception: ${request.method} ${request.requestURI}", e)
            throw e
        } finally {
            val duration = System.currentTimeMillis() - startTime

            val requestBody = getMaskedRequestBody(wrappedRequest)
            val responseBody = getMaskedResponseBody(wrappedResponse)
            val queryString = request.queryString?.let { "?$it" } ?: ""

            val baseLog = "[${request.method}] ${request.requestURI}$queryString"

            log.info("Request: $baseLog\nHeaders: ${headersToString(request)}\nBody: $requestBody")

            if (duration > SLOW_REQUEST_THRESHOLD_MS) {
                log.warn("Slow request: $baseLog (${duration}ms)")
            }

            log.info("Response: $baseLog\nStatus: ${response.status} (${duration}ms)\nBody: $responseBody")

            MDC.clear()
            wrappedResponse.copyBodyToResponse()
        }
    }

    private fun headersToString(request: HttpServletRequest): String {
        return request.headerNames.toList()
            .joinToString(", ") { "$it=${request.getHeader(it)}" }
    }

    private fun getMaskedRequestBody(request: ContentCachingRequestWrapper): String {
        val body = request.contentAsByteArray
        return maskJsonBody(body, request.characterEncoding)
    }

    private fun getMaskedResponseBody(response: ContentCachingResponseWrapper): String {
        val body = response.contentAsByteArray
        return maskJsonBody(body, response.characterEncoding)
    }

    private fun maskJsonBody(bytes: ByteArray, encoding: String): String {
        return try {
            val json = objectMapper.readValue(bytes.toString(Charset.forName(encoding)), MutableMap::class.java)
            val masked = json.entries.associate { (k, v) ->
                val keyStr = k.toString()
                val maskedValue = if (keyStr.lowercase().startsWith("password")) "***" else v
                keyStr to maskedValue
            }
            objectMapper.writeValueAsString(masked)
        } catch (e: Exception) {
            "Non-JSON or unreadable"
        }
    }

}

