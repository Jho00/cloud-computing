package com.example.apiservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate


@Configuration
open class AppConfig {
    @Bean
    open fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}