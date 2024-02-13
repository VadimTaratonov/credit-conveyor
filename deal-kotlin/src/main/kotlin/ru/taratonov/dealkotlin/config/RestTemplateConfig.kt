package ru.taratonov.dealkotlin.config

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import ru.taratonov.dealkotlin.util.DealResponseErrorHandler

@Configuration
class RestTemplateConfig {
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplateBuilder()
            .errorHandler(DealResponseErrorHandler())
            .build()
    }
}