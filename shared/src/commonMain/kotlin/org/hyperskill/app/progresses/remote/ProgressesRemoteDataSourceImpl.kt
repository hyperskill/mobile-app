package org.hyperskill.app.progresses.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.progresses.data.source.ProgressesRemoteDataSource
import org.hyperskill.app.progresses.remote.model.TopicProgressesResponse
import org.hyperskill.app.progresses.remote.model.TrackProgressesResponse
import org.hyperskill.app.topics.domain.model.TopicProgress
import org.hyperskill.app.track.domain.model.TrackProgress

class ProgressesRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : ProgressesRemoteDataSource {
    override suspend fun getTracksProgresses(tracksIds: List<Long>): Result<List<TrackProgress>> =
        kotlin.runCatching {
            httpClient
                .get("/api/progresses") {
                    contentType(ContentType.Application.Json)
                    parameter("ids", tracksIds.joinToString(separator = ",") { "track-$it" })
                }.body<TrackProgressesResponse>().progresses
        }

    override suspend fun getTopicsProgresses(topicsIds: List<Long>): Result<List<TopicProgress>> =
        kotlin.runCatching {
            httpClient
                .get("/api/progresses") {
                    contentType(ContentType.Application.Json)
                    parameter("ids", topicsIds.joinToString(separator = ",") { "topic-$it" })
                }.body<TopicProgressesResponse>().progresses
        }
}