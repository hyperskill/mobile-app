package org.hyperskill.app.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.sentry.HttpStatusCodeRange
import io.sentry.android.okhttp.SentryOkHttpInterceptor

@Suppress("FunctionName")
internal actual fun PreconfiguredHttpClient(block: HttpClientConfig<*>.() -> Unit): HttpClient =
    HttpClient(OkHttp) {
        apply(block)
        engine {
            addInterceptor(
                SentryOkHttpInterceptor(
                    captureFailedRequests = true,
                    failedRequestStatusCodes = listOf(
                        HttpStatusCodeRange(400, 407),
                        HttpStatusCodeRange(409, 599)
                    )
                )
            )
        }
    }