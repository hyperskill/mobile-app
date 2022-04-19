package org.hyperskill.app.auth.remote.source

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.features.feature
import io.ktor.client.features.HttpSend
import io.ktor.http.HttpStatusCode
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.header
import io.ktor.util.AttributeKey
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class BearerTokenHttpClientPlugin(
    private val tokenHeaderName: String,
    private val tokenProvider: () -> String?,
    private val tokenUpdater: suspend () -> Boolean,
    private val tokenExpirationChecker: () -> Boolean,
    private val tokenFailureReporter: suspend () -> Unit
) {

    class Config {
        var tokenHeaderName: String? = null
        var tokenProvider: (() -> String?)? = null
        var tokenUpdater: (suspend () -> Boolean)? = null
        var tokenExpirationChecker: (() -> Boolean)? = null
        var tokenFailureReporter: (suspend () -> Unit)? = null

        fun build(): BearerTokenHttpClientPlugin =
            BearerTokenHttpClientPlugin(
                tokenHeaderName ?: throw IllegalArgumentException("headerName should be passed"),
                tokenProvider ?: throw IllegalArgumentException("tokenProvider should be passed"),
                tokenUpdater ?: throw IllegalArgumentException("tokenUpdater should be passed"),
                tokenExpirationChecker ?: throw IllegalArgumentException("tokenExpirationChecker should be passed"),
                tokenFailureReporter ?: throw IllegalArgumentException("tokenFailureReporter should be passed")
            )
    }

    companion object Feature : HttpClientFeature<Config, BearerTokenHttpClientPlugin> {
        private val refreshTokenMutex = Mutex()

        override val key = AttributeKey<BearerTokenHttpClientPlugin>("TokenFeature")

        override fun prepare(block: Config.() -> Unit): BearerTokenHttpClientPlugin =
            Config().apply(block).build()

        override fun install(feature: BearerTokenHttpClientPlugin, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.State) {
                refreshTokenMutex.withLock {
                    // Check token expiration - if it is expired, update it
                    if (feature.tokenExpirationChecker.invoke()) {
                        // Check if refresh is successful
                        if (!feature.tokenUpdater.invoke()) {
                            feature.tokenFailureReporter.invoke()
                            return@withLock
                        }
                    }

                    val token = feature.tokenProvider.invoke()

                    if (token == null) {
                        feature.tokenFailureReporter.invoke()
                        return@withLock
                    }

                    context.headers.remove(feature.tokenHeaderName)
                    context.header(feature.tokenHeaderName, buildBearerHeader(token))
                }
            }

            val circuitBreaker = AttributeKey<Unit>("TokenFeature_circuitBreaker")
            scope.feature(HttpSend)!!.intercept { httpClientCall, _ ->
                if (httpClientCall.response.status != HttpStatusCode.Unauthorized) return@intercept httpClientCall
                if (httpClientCall.request.attributes.contains(circuitBreaker)) return@intercept httpClientCall

                feature.tokenFailureReporter.invoke()
                return@intercept httpClientCall
            }
        }

        private fun buildBearerHeader(accessToken: String?): String =
            if (accessToken.isNullOrEmpty()) {
                ""
            } else {
                "Bearer $accessToken"
            }
    }
}