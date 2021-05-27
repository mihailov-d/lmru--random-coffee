package ru.leroymerlin.random.coffee.jaeger

import io.opentracing.ScopeManager
import io.opentracing.util.ThreadLocalScopeManager
import io.jaegertracing.internal.JaegerSpanContext
import io.opentracing.Scope
import io.opentracing.Span
import org.slf4j.MDC

class MDCScopeManagerExt private constructor(builder: Builder) : ThreadLocalScopeManager() {
    private val wrappedScopeManager: ThreadLocalScopeManager = builder.scopeManager
    private val mdcTraceIdKey: String
    private val mdcSpanIdKey: String
    private val mdcSampledKey: String
    private val mdcParentSpanIdKey: String
    override fun activate(span: Span): Scope {
        return MDCScope(wrappedScopeManager.activate(span), span)
    }

    override fun activeSpan(): Span? {
        return wrappedScopeManager.activeSpan()
    }

    class Builder {
        internal var scopeManager: ThreadLocalScopeManager = ThreadLocalScopeManager()
        internal var mdcTraceIdKey = "traceId"
        internal var mdcSpanIdKey = "spanId"
        internal var mdcParentSpanIdKey = "parentSpanId"
        internal var mdcSampledKey = "sampled"
        fun withScopeManager(scopeManager: ThreadLocalScopeManager): Builder {
            this.scopeManager = scopeManager
            return this
        }

        fun withMDCTraceIdKey(mdcTraceIdKey: String): Builder {
            this.mdcTraceIdKey = mdcTraceIdKey
            return this
        }

        fun withMDCSpanIdKey(mdcSpanIdKey: String): Builder {
            this.mdcSpanIdKey = mdcSpanIdKey
            return this
        }

        fun withMDCParentSpanIdKey(mdcSpanIdKey: String): Builder {
            this.mdcSpanIdKey = mdcSpanIdKey
            return this
        }

        fun withMDCSampledKey(mdcSampledKey: String): Builder {
            this.mdcSampledKey = mdcSampledKey
            return this
        }

        fun build(): MDCScopeManagerExt {
            return MDCScopeManagerExt(this)
        }
    }

    private open inner class MDCScope constructor(private val wrappedScope: Scope, span: Span) : Scope {
        private val previousTraceId: String? = MDC.get(mdcTraceIdKey)
        private val previousParentSpanId: String? = MDC.get(mdcParentSpanIdKey)
        private val previousSpanId: String? = MDC.get(mdcSpanIdKey)
        private val previousSampled: String? = MDC.get(mdcSampledKey)

        protected fun putContext(spanContext: JaegerSpanContext) {
            val parentSpanId = if (spanContext.parentId != 0L) java.lang.Long.toHexString(spanContext.parentId) else null
            replace(mdcTraceIdKey, spanContext.toTraceId())
            replace(mdcParentSpanIdKey, parentSpanId)
            replace(mdcSpanIdKey, spanContext.toSpanId())
            replace(mdcSampledKey, spanContext.isSampled.toString())
        }

        private fun replace(key: String, value: String?) {
            if (value == null) {
                MDC.remove(key)
            } else {
                MDC.put(key, value)
            }
        }

        override fun close() {
            wrappedScope.close()
            replace(mdcTraceIdKey, previousTraceId)
            replace(mdcParentSpanIdKey, previousParentSpanId)
            replace(mdcSpanIdKey, previousSpanId)
            replace(mdcSampledKey, previousSampled)
        }

        init {
            if (span.context() is JaegerSpanContext) {
                putContext(span.context() as JaegerSpanContext)
            }
        }
    }

    init {
        mdcTraceIdKey = builder.mdcTraceIdKey
        mdcParentSpanIdKey = builder.mdcParentSpanIdKey
        mdcSpanIdKey = builder.mdcSpanIdKey
        mdcSampledKey = builder.mdcSampledKey
    }
}