package org.hyperskill.app.core.domain.url

import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.Url
import org.hyperskill.app.config.BuildKonfig

object HyperskillUrlBuilder {
    fun build(path: HyperskillUrlPath): Url {
        val urlBuilder = URLBuilder(
            protocol = URLProtocol.HTTPS,
            host = BuildKonfig.HOST,
            pathSegments = path.path.split("/")
        )
        return Url(urlBuilder)
    }
}