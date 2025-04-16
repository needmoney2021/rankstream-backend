package com.rankstream.backend.config.web

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter
import org.springframework.web.accept.ContentNegotiationStrategy
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebApplicationConfig(

    @Value("\${cors.origins}")
    private val origins: String,

    @Value("\${cors.methods}")
    private val methods: String,

    @Value("\${cors.headers}")
    private val headers: String
) : WebMvcConfigurer {

    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper()
        .registerModule(JavaTimeModule())
        .registerKotlinModule()
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)


    @Bean
    fun xmlMapper(): XmlMapper = XmlMapper()

    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
        configurer
            .favorParameter(false)
            .ignoreAcceptHeader(false)
            .defaultContentTypeStrategy(ContentNegotiationStrategy { request ->
                val acceptHeader = request.getHeader("Accept")

                when {
                    acceptHeader?.contains("application/xml") == true -> listOf(MediaType.APPLICATION_XML)
                    else -> listOf(MediaType.APPLICATION_JSON)
                }
            })
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .maxAge(3600)
            .allowedOrigins(origins)
            .allowedMethods(methods)
            .allowedHeaders(headers)
            .allowCredentials(true)
    }

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>?>) {
        converters.add(MappingJackson2HttpMessageConverter(objectMapper()))
        converters.add(MappingJackson2XmlHttpMessageConverter(xmlMapper()))
    }

}
