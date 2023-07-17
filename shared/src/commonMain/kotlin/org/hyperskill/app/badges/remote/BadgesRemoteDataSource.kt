package org.hyperskill.app.badges.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.badges.domain.data.source.BadgesDataSource
import org.hyperskill.app.badges.domain.model.Badge

class BadgesRemoteDataSource(
    private val httpClient: HttpClient
) : BadgesDataSource {
    override suspend fun getReceivedBadges(): Result<List<Badge>> =
        runCatching {
            httpClient.get("/api/badges") {
                contentType(ContentType.Application.Json)
            }.body<BadgesResponse>().badges
        }
}