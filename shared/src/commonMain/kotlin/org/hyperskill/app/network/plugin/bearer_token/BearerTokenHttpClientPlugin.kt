package org.hyperskill.app.network.plugin.bearer_token

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.plugin
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.util.AttributeKey
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class BearerTokenHttpClientPlugin(
    private val authMutex: Mutex,
    private val tokenProvider: () -> String?,
    private val tokenUpdater: suspend () -> TokenRefreshResult,
    private val tokenExpirationChecker: () -> Boolean,
    private val tokenRefreshFailedReporter: () -> Unit
) {

    class Config {
        var authMutex: Mutex? = null
        var tokenProvider: (() -> String?)? = null
        var tokenUpdater: (suspend () -> TokenRefreshResult)? = null
        var tokenExpirationChecker: (() -> Boolean)? = null
        var tokenRefreshFailedReporter: (() -> Unit)? = null

        fun build(): BearerTokenHttpClientPlugin =
            BearerTokenHttpClientPlugin(
                authMutex ?: error("authorization mutex should be passed"),
                tokenProvider ?: error("tokenProvider should be passed"),
                tokenUpdater ?: error("tokenUpdater should be passed"),
                tokenExpirationChecker ?: error("tokenExpirationChecker should be passed"),
                tokenRefreshFailedReporter ?: error("tokenRefreshFailedReporter should be passed")
            )
    }

    companion object Plugin : HttpClientPlugin<Config, BearerTokenHttpClientPlugin> {
        override val key = AttributeKey<BearerTokenHttpClientPlugin>("Tokenplugin")

        override fun prepare(block: Config.() -> Unit): BearerTokenHttpClientPlugin =
            Config().apply(block).build()

        override fun install(plugin: BearerTokenHttpClientPlugin, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.State) {
                plugin.authMutex.withLock {
                    // Check token expiration - if it is expired, update it
                    if (plugin.tokenExpirationChecker.invoke()) {
                        val tokenRefreshResult = plugin.tokenUpdater.invoke()
                        val shouldDeauthorizeUser =
                            tokenRefreshResult is TokenRefreshResult.Failed &&
                                tokenRefreshResult.shouldDeauthorizeUser
                        if (shouldDeauthorizeUser) {
                            plugin.tokenRefreshFailedReporter.invoke()
                            return@withLock
                        }
                    }

                    val token = plugin.tokenProvider.invoke()
                    if (token == null) {
                        plugin.tokenRefreshFailedReporter.invoke()
                        return@withLock
                    }

                    context.headers.remove(HttpHeaders.Authorization)
                    context.header(HttpHeaders.Authorization, buildBearerHeader(token))
                }
            }

            val circuitBreaker = AttributeKey<Unit>("Tokenplugin_circuitBreaker")
            scope.plugin(HttpSend).intercept { context ->
                val origin = execute(context)
                if (origin.response.status != HttpStatusCode.Forbidden) return@intercept origin
                if (origin.request.attributes.contains(circuitBreaker)) return@intercept origin

                plugin.tokenRefreshFailedReporter.invoke()
                return@intercept origin
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