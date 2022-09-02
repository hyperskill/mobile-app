package org.hyperskill.app.auth.remote.source

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.HttpSendPipeline
import io.ktor.client.statement.HttpReceivePipeline
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.Cookie
import io.ktor.http.HttpHeaders
import io.ktor.http.Url
import io.ktor.http.clone
import io.ktor.http.parseClientCookiesHeader
import io.ktor.http.renderCookieHeader
import io.ktor.http.setCookie
import io.ktor.util.AttributeKey
import io.ktor.utils.io.core.Closeable
import org.hyperskill.app.config.BuildKonfig

class HttpCookiesPlugin(
    private val storage: CookiesStorage,
    private val shouldSendCookiesForRequest: Boolean
) : Closeable {
    class Config {
        var storage: CookiesStorage? = null
        var shouldSendCookiesForRequest: Boolean? = null

        fun build(): HttpCookiesPlugin =
            HttpCookiesPlugin(
                storage ?: throw IllegalArgumentException("storage should be passed"),
                shouldSendCookiesForRequest ?: throw IllegalArgumentException("shouldSendCookiesForRequest should be passed")
            )
    }

    private suspend fun get(requestUrl: Url): List<Cookie> =
        storage.get(requestUrl)

    private suspend fun captureHeaderCookies(builder: HttpRequestBuilder) {
        with(builder) {
            val url = builder.url.clone().build()
            val cookies = headers[HttpHeaders.Cookie]?.let { cookieHeader ->
                parseClientCookiesHeader(cookieHeader).map { (name, encodedValue) -> Cookie(name, encodedValue) }
            }
            cookies?.forEach { storage.addCookie(url, it) }
        }
    }

    private suspend fun sendCookiesWith(builder: HttpRequestBuilder) {
        val cookies = get(builder.url.clone().build())

        with(builder) {
            if (cookies.isNotEmpty()) {
                headers[HttpHeaders.Cookie] = renderCookies(cookies)
                headers[HttpHeaders.Origin] = "https://" + BuildKonfig.HOST

                val tokenCookie = cookies
                    .firstOrNull { it.name == COOKIE_NAME_CSRFTOKEN }
                if (tokenCookie != null) {
                    headers[HEADER_X_CSRFTOKEN] = tokenCookie.value
                }
            } else {
                headers.remove(HttpHeaders.Cookie)
            }
        }
    }

    private fun renderCookies(cookies: List<Cookie>): String =
        cookies.joinToString(";", transform = ::renderCookieHeader)

    private suspend fun saveCookiesFrom(response: HttpResponse) {
        val url = response.request.url
        response.setCookie().forEach {
            storage.addCookie(url, it)
        }
    }

    override fun close() {
        storage.close()
    }

    companion object : HttpClientPlugin<Config, HttpCookiesPlugin> {
        private const val COOKIE_NAME_CSRFTOKEN = "csrftoken"
        private const val HEADER_X_CSRFTOKEN = "X-CSRFToken"

        override val key: AttributeKey<HttpCookiesPlugin> = AttributeKey("HttpCookiesPlugin")

        override fun prepare(block: Config.() -> Unit): HttpCookiesPlugin =
            Config().apply(block).build()

        override fun install(plugin: HttpCookiesPlugin, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.State) {
                plugin.captureHeaderCookies(context)
            }
            scope.sendPipeline.intercept(HttpSendPipeline.State) {
                if (plugin.shouldSendCookiesForRequest) {
                    plugin.sendCookiesWith(context)
                }
            }
            scope.receivePipeline.intercept(HttpReceivePipeline.State) { response ->
                plugin.saveCookiesFrom(response)
            }
        }
    }
}
