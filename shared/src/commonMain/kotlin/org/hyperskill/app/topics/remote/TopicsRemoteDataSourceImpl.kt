package org.hyperskill.app.topics.remote

import io.ktor.client.call.body
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.topics.data.source.TopicsRemoteDataSource
import org.hyperskill.app.topics.domain.model.Topic
import org.hyperskill.app.topics.remote.model.TopicsResponse

class TopicsRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : TopicsRemoteDataSource {
    override suspend fun getTopicsByIds(topicsIds: List<Long>): Result<List<Topic>> =
        kotlin.runCatching {
            httpClient.get("/api/topics") {
                contentType(ContentType.Application.Json)
                parameter("ids", topicsIds.joinToString(","))
            }.body<TopicsResponse>().topics
        }
}