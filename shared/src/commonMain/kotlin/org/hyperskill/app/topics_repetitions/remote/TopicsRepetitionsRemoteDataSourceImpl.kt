package org.hyperskill.app.topics_repetitions.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.network.remote.parameterPage
import org.hyperskill.app.network.remote.parameterPageSize
import org.hyperskill.app.topics_repetitions.data.source.TopicsRepetitionsRemoteDataSource
import org.hyperskill.app.topics_repetitions.domain.model.TopicRepetition
import org.hyperskill.app.topics_repetitions.domain.model.TopicRepetitionStatistics
import org.hyperskill.app.topics_repetitions.remote.model.TopicsRepetitionStatisticsResponse
import org.hyperskill.app.topics_repetitions.remote.model.TopicsRepetitionsResponse

internal class TopicsRepetitionsRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : TopicsRepetitionsRemoteDataSource {

    companion object {
        private const val PARAM_IS_IN_CURRENT_TRACK = "is_in_current_track"
    }

    override suspend fun getTopicsRepetitions(
        pageSize: Int,
        page: Int,
        isInCurrentTrack: Boolean
    ): Result<List<TopicRepetition>> =
        kotlin.runCatching {
            httpClient.get("/api/topics-repetition") {
                contentType(ContentType.Application.Json)
                parameterPageSize(pageSize)
                parameterPage(page)
                parameter(PARAM_IS_IN_CURRENT_TRACK, isInCurrentTrack)
            }.body<TopicsRepetitionsResponse>().topicsRepetitions
        }

    override suspend fun getTopicsRepetitionStatistics(isInCurrentTrack: Boolean): Result<TopicRepetitionStatistics> =
        kotlin.runCatching {
            httpClient.get("/api/topics-repetition/statistics") {
                contentType(ContentType.Application.Json)
                parameter(PARAM_IS_IN_CURRENT_TRACK, isInCurrentTrack)
            }.body<TopicsRepetitionStatisticsResponse>().topicsRepetitionStatistics.first()
        }
}