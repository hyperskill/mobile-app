package org.hyperskill.app.topics_repetitions.remote

import io.ktor.client.call.body
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.topics_repetitions.data.source.TopicsRepetitionsRemoteDataSource
import org.hyperskill.app.topics_repetitions.domain.model.TopicsRepetitions
import org.hyperskill.app.topics_repetitions.remote.model.TopicsRepetitionsResponse

class TopicsRepetitionsRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : TopicsRepetitionsRemoteDataSource {
    override suspend fun getTopicsRepetitions(isCurrentTrack: Boolean): Result<TopicsRepetitions> =
        kotlin.runCatching {
            httpClient.get("/api/topics-repetitions") {
                contentType(ContentType.Application.Json)
                parameter("is_current_track", isCurrentTrack)
            }.body<TopicsRepetitionsResponse>().topicsRepetitions.first()
        }
}