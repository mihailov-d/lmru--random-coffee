package ru.leroymerlin.random.coffee.configuration

import io.micrometer.core.aop.TimedAspect
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.MeterRegistry.Config
import io.micrometer.core.instrument.config.MeterFilter.denyUnless
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MetricsConfig {

    @Bean
    fun timedAspect(registry: MeterRegistry?): TimedAspect? {
        return TimedAspect(registry!!)
    }

    @Bean
    fun filters(registry: MeterRegistry): Config =
        registry.config()
            .meterFilter(
                denyUnless {
                    val tag = it.getTag("uri")
                    tag == null || tag.startsWith("/api/")
                }
            )
}
