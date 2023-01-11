package org.hyperskill.app.core.domain.url

import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.Url
import org.hyperskill.app.network.domain.model.NetworkEndpointConfigInfo

class HyperskillUrlBuilder(
    private val networkEndpointConfigInfo: NetworkEndpointConfigInfo
) {
    fun build(path: HyperskillUrlPath): Url {
        val urlBuilder = URLBuilder(
            protocol = URLProtocol.HTTPS,
            host = networkEndpointConfigInfo.host,
            pathSegments = path.path.split("/")
        )
        return Url(urlBuilder)
    }
}