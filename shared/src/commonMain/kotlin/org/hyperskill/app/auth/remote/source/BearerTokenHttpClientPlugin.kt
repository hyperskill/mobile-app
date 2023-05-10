package org.hyperskill.app.auth.remote.source

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.plugin
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.header
import io.ktor.http.HttpStatusCode
import io.ktor.util.AttributeKey
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class BearerTokenHttpClientPlugin(
    private val authMutex: Mutex,
    private val tokenHeaderName: String,
    private val tokenProvider: () -> String?,
    private val tokenUpdater: suspend () -> Boolean,
    private val tokenExpirationChecker: () -> Boolean,
    private val tokenFailureReporter: () -> Unit
) {

    class Config {
        var authMutex: Mutex? = null
        var tokenHeaderName: String? = null
        var tokenProvider: (() -> String?)? = null
        var tokenUpdater: (suspend () -> Boolean)? = null
        var tokenExpirationChecker: (() -> Boolean)? = null
        var tokenFailureReporter: (() -> Unit)? = null

        fun build(): BearerTokenHttpClientPlugin =
            BearerTokenHttpClientPlugin(
                authMutex ?: throw IllegalArgumentException("authorization mutex should be passed"),
                tokenHeaderName ?: throw IllegalArgumentException("headerName should be passed"),
                tokenProvider ?: throw IllegalArgumentException("tokenProvider should be passed"),
                tokenUpdater ?: throw IllegalArgumentException("tokenUpdater should be passed"),
                tokenExpirationChecker ?: throw IllegalArgumentException("tokenExpirationChecker should be passed"),
                tokenFailureReporter ?: throw IllegalArgumentException("tokenFailureReporter should be passed")
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
                        // Check if refresh is successful
                        if (!plugin.tokenUpdater.invoke()) {
                            plugin.tokenFailureReporter.invoke()
                            return@withLock
                        }
                    }

                    val token = plugin.tokenProvider.invoke()

                    if (token == null) {
                        plugin.tokenFailureReporter.invoke()
                        return@withLock
                    }

                    context.headers.remove(plugin.tokenHeaderName)
                    context.header(plugin.tokenHeaderName, buildBearerHeader(token))
                }
            }

            val circuitBreaker = AttributeKey<Unit>("Tokenplugin_circuitBreaker")
            scope.plugin(HttpSend).intercept { context ->
                val origin = execute(context)
                if (origin.response.status != HttpStatusCode.Forbidden) return@intercept origin
                if (origin.request.attributes.contains(circuitBreaker)) return@intercept origin

                plugin.tokenFailureReporter.invoke()
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