package org.hyperskill.app.auth.remote.source

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.header
import io.ktor.util.AttributeKey
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class BearerToken(
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

        fun build(): BearerToken =
            BearerToken(
                tokenHeaderName ?: throw IllegalArgumentException("headerName should be passed"),
                tokenProvider ?: throw IllegalArgumentException("tokenProvider should be passed"),
                tokenUpdater ?: throw IllegalArgumentException("tokenUpdater should be passed"),
                tokenExpirationChecker ?: throw IllegalArgumentException("tokenExpirationChecker should be passed")
            )
    }

    companion object Feature : HttpClientFeature<Config, BearerToken> {
        private val refreshTokenMutex = Mutex()

        override val key = AttributeKey<BearerToken>("TokenFeature")

        override fun prepare(block: Config.() -> Unit): BearerToken =
            Config().apply(block).build()

        override fun install(feature: BearerToken, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.State) {
                refreshTokenMutex.withLock {
                    // Check if token is expired - if it is valid, use it
                    if (!feature.tokenExpirationChecker.invoke()) {
                        feature.tokenProvider.invoke()?.let { token ->
                            context.headers.remove(feature.tokenHeaderName)
                            context.header(feature.tokenHeaderName, buildBearerHeader(token))
                        }
                    } else {
                        // Check if refresh is successful
                        if (!feature.tokenUpdater.invoke()) {
                            return@withLock
                        }

                        // Obtain new token
                        val token = feature.tokenProvider.invoke() ?: return@withLock

                        context.headers.remove(feature.tokenHeaderName)
                        context.header(feature.tokenHeaderName, buildBearerHeader(token))
                    }
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