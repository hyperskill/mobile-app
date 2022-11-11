package org.hyperskill.app.magic_links.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.magic_links.data.source.MagicLinksRemoteDataSource
import org.hyperskill.app.magic_links.domain.model.MagicLink
import org.hyperskill.app.magic_links.remote.model.MagicLinksRequest
import org.hyperskill.app.magic_links.remote.model.MagicLinksResponse

class MagicLinksRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : MagicLinksRemoteDataSource {
    override suspend fun createMagicLink(nextUrl: String): Result<MagicLink> =
        kotlin.runCatching {
            httpClient
                .post("/api/magic-links") {
                    contentType(ContentType.Application.Json)
                    setBody(MagicLinksRequest(nextUrl.ifEmpty { "/" }))
                }.body<MagicLinksResponse>().magicLinks.first()
        }
}
