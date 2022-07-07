package org.hyperskill.app.track.remote

import org.hyperskill.app.track.data.source.TrackRemoteDataSource
import org.hyperskill.app.track.domain.model.Track
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.client.request.parameter
import org.hyperskill.app.track.domain.model.StudyPlan
import org.hyperskill.app.track.domain.model.TrackProgress
import org.hyperskill.app.track.remote.model.StudyPlanResponse
import org.hyperskill.app.track.remote.model.TrackProgressResponse
import org.hyperskill.app.track.remote.model.TrackResponse

class TrackRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : TrackRemoteDataSource {
    override suspend fun getTracks(trackIds: List<Long>): Result<List<Track>> =
        kotlin.runCatching {
            httpClient
                .get("/api/tracks") {
                    contentType(ContentType.Application.Json)
                    parameter("ids", trackIds.joinToString(separator = ","))
                }.body<TrackResponse>().tracks
        }

    override suspend fun getTracksProgresses(trackIds: List<Long>): Result<List<TrackProgress>> =
        kotlin.runCatching {
            httpClient
                .get("/api/progresses") {
                    contentType(ContentType.Application.Json)
                    parameter("ids", trackIds.joinToString(separator = ",") { "track-$it" })
                }.body<TrackProgressResponse>().progresses
        }

    override suspend fun getStudyPlans(): Result<List<StudyPlan>> =
        kotlin.runCatching {
            httpClient
                .get("/api/study-plans") {
                    contentType(ContentType.Application.Json)
                }.body<StudyPlanResponse>().studyPlans
        }
}