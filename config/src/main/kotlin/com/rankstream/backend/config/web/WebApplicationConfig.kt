package com.rankstream.backend.config.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.springframework.beans.factory.annotation.Value
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

    private val objectMapper: ObjectMapper,

    private val xmlMapper: XmlMapper,

    @Value("\${cors.origins}")
    private val origins: String,

    @Value("\${cors.methods}")
    private val methods: String,

    @Value("\${cors.headers}")
    private val headers: String
) : WebMvcConfigurer {

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
            .allowedOrigins(this.origins)
            .allowedMethods(this.methods)
            .allowedHeaders(this.headers)
            .allowCredentials(true)
    }

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>?>) {
        converters.add(MappingJackson2HttpMessageConverter(this.objectMapper))
        converters.add(MappingJackson2XmlHttpMessageConverter(this.xmlMapper))
    }

}
