package org.hyperskill.app.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

@Suppress("FunctionName")
internal expect fun PreconfiguredHttpClient(block: HttpClientConfig<*>.() -> Unit): HttpClient