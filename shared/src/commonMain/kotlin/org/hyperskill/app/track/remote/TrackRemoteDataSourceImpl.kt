package org.hyperskill.app.track.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.track.data.source.TrackRemoteDataSource
import org.hyperskill.app.track.domain.model.StudyPlan
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.remote.model.StudyPlanResponse
import org.hyperskill.app.track.remote.model.TrackResponse

class TrackRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : TrackRemoteDataSource {
    override suspend fun getTracks(trackIds: List<Long>?): Result<List<Track>> =
        kotlin.runCatching {
            httpClient
                .get("/api/tracks") {
                    contentType(ContentType.Application.Json)
                    if (trackIds != null) {
                        parameter("ids", trackIds.joinToString(separator = ","))
                    }
                }.body<TrackResponse>().tracks
        }

    override suspend fun getStudyPlans(): Result<List<StudyPlan>> =
        kotlin.runCatching {
            httpClient
                .get("/api/study-plans") {
                    contentType(ContentType.Application.Json)
                }.body<StudyPlanResponse>().studyPlans
        }
}