package org.hyperskill.app.topics_repetitions.remote

import io.ktor.client.call.body
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.topics_repetitions.data.source.TopicsRepetitionsRemoteDataSource
import org.hyperskill.app.topics_repetitions.domain.model.TopicRepetition
import org.hyperskill.app.topics_repetitions.domain.model.TopicsRepetitionStatistics
import org.hyperskill.app.topics_repetitions.remote.model.TopicsRepetitionStatisticsResponse
import org.hyperskill.app.topics_repetitions.remote.model.TopicsRepetitionsResponse

class TopicsRepetitionsRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : TopicsRepetitionsRemoteDataSource {
    companion object {
        const val TOPICS_REPETITIONS_ORDERING_FIELD = "next_repeat_at"
    }

    override suspend fun getTopicsRepetitions(pageSize: Int, page: Int): Result<List<TopicRepetition>> =
        kotlin.runCatching {
            httpClient.get("/api/topics-repetition") {
                contentType(ContentType.Application.Json)
                parameter("page_size", pageSize)
                parameter("page", page)
                parameter("ordering", TOPICS_REPETITIONS_ORDERING_FIELD)
            }.body<TopicsRepetitionsResponse>().topicsRepetitions
        }

    override suspend fun getTopicsRepetitionStatistics(): Result<TopicsRepetitionStatistics> =
        kotlin.runCatching {
            httpClient.get("/api/topics-repetition/statistics") {
                contentType(ContentType.Application.Json)
            }.body<TopicsRepetitionStatisticsResponse>().topicsRepetitionStatistics.first()
        }
}