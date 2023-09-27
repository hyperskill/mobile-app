package org.hyperskill.app.profile.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.datetime.TimeZone
import org.hyperskill.app.profile.data.source.ProfileRemoteDataSource
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.remote.model.ProfileResponse
import org.hyperskill.app.profile.remote.model.ProfileSelectTrackRequest
import org.hyperskill.app.profile.remote.model.ProfileSelectTrackWithProjectRequest
import org.hyperskill.app.profile.remote.model.ProfileSetNotificationHourRequest
import org.hyperskill.app.profile.remote.model.ProfileSetNotificationHourWithTimeZoneRequest
import org.hyperskill.app.profile.remote.model.ProfileSetTimeZoneRequest

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
                .putProfile(
                    profileId = profileId,
                    body = ProfileSelectTrackWithProjectRequest(trackId, projectId)
                )
        }

    override suspend fun selectTrack(profileId: Long, trackId: Long): Result<Profile> =
        kotlin.runCatching {
            httpClient
                .putProfile(
                    profileId = profileId,
                    body = ProfileSelectTrackRequest(trackId)
                )
        }

    override suspend fun setDailyStudyReminderNotificationHour(
        profileId: Long,
        notificationHour: Int,
        timeZone: TimeZone
    ): Result<Profile> =
        runCatching {
            httpClient
                .putProfile(
                    profileId = profileId,
                    body = ProfileSetNotificationHourWithTimeZoneRequest(
                        notificationHour = notificationHour,
                        timeZone = timeZone
                    )
                )
        }

    override suspend fun setDailyStudyReminderNotificationHour(profileId: Long, notificationHour: Int?): Result<Profile> =
        runCatching {
            httpClient
                .putProfile(
                    profileId = profileId,
                    body = ProfileSetNotificationHourRequest(notificationHour = notificationHour)
                )
        }

    override suspend fun setTimeZone(profileId: Long, timeZone: TimeZone): Result<Profile> =
        runCatching {
            httpClient
                .putProfile(
                    profileId = profileId,
                    body = ProfileSetTimeZoneRequest(timeZone)
                )
        }

    private suspend inline fun <reified T> HttpClient.putProfile(profileId: Long, body: T): Profile =
        put("/api/profiles/$profileId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body<ProfileResponse>().profiles.first()
}