package org.hyperskill.app.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.darwin.Darwin

@Suppress("FunctionName")
internal actual fun PreconfiguredPlatformHttpClient(block: HttpClientConfig<*>.() -> Unit): HttpClient =
    HttpClient(Darwin) {
        apply(block)
        engine {
            configureSession {
                timeoutIntervalForRequest = 10.0
            }
        }
    }