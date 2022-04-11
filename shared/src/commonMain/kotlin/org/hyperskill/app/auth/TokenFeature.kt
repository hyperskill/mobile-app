package org.hyperskill.app.auth

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.features.feature
import io.ktor.client.features.HttpSend
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.header
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.util.AttributeKey
import kotlinx.coroutines.sync.Mutex

class TokenFeature(
    private val tokenHeaderName: String,
    private val tokenProvider: () -> String?,
    private val tokenUpdater: suspend () -> Boolean,
    private val sameTokenChecker: (HttpRequest) -> Boolean,
    private val tokenExpirationChecker: () -> Boolean
) {

    // TODO Add channel for AppFeature
    class Config {
        var tokenHeaderName: String? = null
        var tokenProvider: (() -> String?)? = null
        var tokenUpdater: (suspend () -> Boolean)? = null
        var sameTokenChecker: ((HttpRequest) -> Boolean)? = null
        var tokenExpirationChecker: (() -> Boolean)? = null

        fun build() = TokenFeature(
            tokenHeaderName ?: throw IllegalArgumentException("headerName should be passed"),
            tokenProvider ?: throw IllegalArgumentException("tokenProvider should be passed"),
            tokenUpdater ?: throw IllegalArgumentException("tokenUpdater should be passed"),
            sameTokenChecker ?: throw IllegalArgumentException("sameTokenChecker should be passed"),
            tokenExpirationChecker ?: throw IllegalArgumentException("tokenExpirationChecker should be passed")
        )
    }

    companion object Feature : HttpClientFeature<Config, TokenFeature> {

        private val refreshTokenMutex = Mutex()

        override val key = AttributeKey<TokenFeature>("TokenFeature")

        override fun prepare(block: Config.() -> Unit) = Config().apply(block).build()

        override fun install(feature: TokenFeature, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.State) {
                feature.tokenProvider.invoke().let { token ->
                    context.headers.remove(feature.tokenHeaderName)
                    context.header(feature.tokenHeaderName, "Bearer $token")
                }
            }
            val circuitBreaker = AttributeKey<Unit>("TokenFeature_circuitBreaker")
            scope.feature(HttpSend)!!.intercept { origin, context ->
                // Check token for expiration
                if (!feature.tokenExpirationChecker.invoke()) return@intercept origin
                // If this interceptor has already been called, don't do anything
                if (origin.request.attributes.contains(circuitBreaker)) return@intercept origin



                // Lock here, so other requests will wait until the token is updated
                refreshTokenMutex.lock()

                // If the token of the request is the same, then the token
                // hasn't been updated yet and we should update it

                if (feature.sameTokenChecker.invoke(origin.request)) {
                    val refreshSuccess = feature.tokenUpdater.invoke()
                    // If token refresh failed, then just let the token error occur
                    if (!refreshSuccess) {
                        refreshTokenMutex.unlock()
                        return@intercept origin
                    }
                }

                val token = feature.tokenProvider.invoke()
                // If the token was removed, then just let the token error occur
                if (token == null) {
                    refreshTokenMutex.unlock()
                    return@intercept origin
                }

                // Execute the same request with a new token
                val request = HttpRequestBuilder()
                request.takeFrom(context)
                request.headers[feature.tokenHeaderName] = "Bearer $token"
                request.attributes.put(circuitBreaker, Unit)

                refreshTokenMutex.unlock()

                return@intercept execute(request)
            }
        }
    }
}