package org.hyperskill.app.auth.remote.source

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.header
import io.ktor.util.AttributeKey
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class BearerTokenHttpClientPlugin(
    private val tokenHeaderName: String,
    private val tokenProvider: () -> String?,
    private val tokenUpdater: suspend () -> Boolean,
    private val tokenExpirationChecker: () -> Boolean
) {

    // TODO Add channel for AppFeature
    class Config {
        var tokenHeaderName: String? = null
        var tokenProvider: (() -> String?)? = null
        var tokenUpdater: (suspend () -> Boolean)? = null
        var tokenExpirationChecker: (() -> Boolean)? = null

        fun build(): BearerTokenHttpClientPlugin =
            BearerTokenHttpClientPlugin(
                tokenHeaderName ?: throw IllegalArgumentException("headerName should be passed"),
                tokenProvider ?: throw IllegalArgumentException("tokenProvider should be passed"),
                tokenUpdater ?: throw IllegalArgumentException("tokenUpdater should be passed"),
                tokenExpirationChecker ?: throw IllegalArgumentException("tokenExpirationChecker should be passed")
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
                            return@withLock
                        }
                    }

                    val token = feature.tokenProvider.invoke() ?: return@withLock
                    context.headers.remove(feature.tokenHeaderName)
                    context.header(feature.tokenHeaderName, buildBearerHeader(token))
                }
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