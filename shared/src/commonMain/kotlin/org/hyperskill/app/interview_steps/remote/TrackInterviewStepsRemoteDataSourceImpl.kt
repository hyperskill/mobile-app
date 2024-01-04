package org.hyperskill.app.interview_steps.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.interview_steps.data.source.TrackInterviewStepsRemoteDataSource
import org.hyperskill.app.interview_steps.remote.model.TrackInterviewStepsResponse
import org.hyperskill.app.network.remote.parameterPage
import org.hyperskill.app.network.remote.parameterPageSize

internal class TrackInterviewStepsRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : TrackInterviewStepsRemoteDataSource {
    override suspend fun getTrackInterviewSteps(
        pageSize: Int,
        page: Int
    ): Result<TrackInterviewStepsResponse> =
        kotlin.runCatching {
            httpClient
                .get("/api/track-interview-steps") {
                    contentType(ContentType.Application.Json)
                    parameterPageSize(pageSize)
                    parameterPage(page)
                }
                .body<TrackInterviewStepsResponse>()
        }
}