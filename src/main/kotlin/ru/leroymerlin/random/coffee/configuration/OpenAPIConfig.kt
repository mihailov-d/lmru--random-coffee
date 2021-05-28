package ru.leroymerlin.random.coffee.configuration

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class OpenAPIConfig {

        @Bean
    open fun customOpenAPI(@Value("\${brick.version}") appVersion: String? = null) =
        OpenAPI().info(
            Info()
                .description("Random Coffee REST API for web application")
                .title("Random Coffee REST").version(appVersion)
        )!!
}
