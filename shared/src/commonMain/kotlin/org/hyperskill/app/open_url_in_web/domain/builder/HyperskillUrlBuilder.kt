package org.hyperskill.app.open_url_in_web.domain.builder

import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.Url
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.open_url_in_web.domain.model.HyperskillUrlPath

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