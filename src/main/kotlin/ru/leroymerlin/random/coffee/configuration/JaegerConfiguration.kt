package ru.leroymerlin.random.coffee.configuration

import io.jaegertracing.internal.JaegerTracer.Builder
import io.opentracing.contrib.java.spring.jaeger.starter.TracerBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.leroymerlin.random.coffee.jaeger.MDCScopeManagerExt

@Configuration
class JaegerConfiguration {

    @Bean
    fun mdcBuilderCustomizer(): TracerBuilderCustomizer? {
        return TracerBuilderCustomizer { builder: Builder ->
            builder.withScopeManager(
                MDCScopeManagerExt.Builder().build()
            )
        }
    }
}
