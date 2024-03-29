package org.hyperskill.app.profile.data.source

import kotlinx.datetime.TimeZone
import org.hyperskill.app.profile.domain.model.Profile

interface ProfileRemoteDataSource {
    suspend fun getCurrentProfile(): Result<Profile>

    suspend fun selectTrackWithProject(profileId: Long, trackId: Long, projectId: Long): Result<Profile>

    suspend fun selectTrack(profileId: Long, trackId: Long): Result<Profile>

    suspend fun setDailyStudyReminderNotificationHour(
        profileId: Long,
        notificationHour: Int,
        timeZone: TimeZone
    ): Result<Profile>

    suspend fun setDailyStudyReminderNotificationHour(profileId: Long, notificationHour: Int?): Result<Profile>

    suspend fun setTimeZone(profileId: Long, timeZone: TimeZone): Result<Profile>
}