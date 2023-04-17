package org.hyperskill.app.profile.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.profile.data.source.ProfileRemoteDataSource
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.remote.model.ProfileResponse
import org.hyperskill.app.profile.remote.model.ProfileSelectTrackRequest
import org.hyperskill.app.profile.remote.model.ProfileSelectTrackWithProjectRequest

class ProfileRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : ProfileRemoteDataSource {
    override suspend fun getCurrentProfile(): Result<Profile> =
        kotlin.runCatching {
            httpClient
                .get("/api/profiles/current") {
                    contentType(ContentType.Application.Json)
                }.body<ProfileResponse>().profiles.first()
        }

    override suspend fun selectTrackWithProject(profileId: Long, trackId: Long, projectId: Long): Result<Profile> =
        kotlin.runCatching {
            httpClient
                .put("/api/profiles/$profileId") {
                    contentType(ContentType.Application.Json)
                    setBody(ProfileSelectTrackWithProjectRequest(trackId, projectId))
                }.body<ProfileResponse>().profiles.first()
        }

    override suspend fun selectTrack(profileId: Long, trackId: Long): Result<Profile> =
        kotlin.runCatching {
            httpClient
                .put("/api/profiles/$profileId") {
                    contentType(ContentType.Application.Json)
                    setBody(ProfileSelectTrackRequest(trackId))
                }.body<ProfileResponse>().profiles.first()
        }
}