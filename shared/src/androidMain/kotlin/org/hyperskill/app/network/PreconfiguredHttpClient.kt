package org.hyperskill.app.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.sentry.okhttp.SentryOkHttpInterceptor

@Suppress("FunctionName")
internal actual fun PreconfiguredPlatformHttpClient(block: HttpClientConfig<*>.() -> Unit): HttpClient =
    HttpClient(OkHttp) {
        apply(block)
        engine {
            addInterceptor(
                SentryOkHttpInterceptor(captureFailedRequests = false)
            )
        }
    }