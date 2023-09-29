package org.hyperskill.app.profile.data.repository

import kotlinx.datetime.TimeZone
import org.hyperskill.app.profile.data.source.ProfileRemoteDataSource
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val profileRemoteDataSource: ProfileRemoteDataSource
) : ProfileRepository {
    override suspend fun selectTrackWithProject(profileId: Long, trackId: Long, projectId: Long): Result<Profile> =
        profileRemoteDataSource.selectTrackWithProject(profileId, trackId, projectId)

    override suspend fun selectTrack(profileId: Long, trackId: Long): Result<Profile> =
        profileRemoteDataSource.selectTrack(profileId, trackId)

    override suspend fun setDailyStudyReminderNotificationHour(
        profileId: Long,
        notificationHour: Int,
        timeZone: TimeZone
    ): Result<Profile> =
        profileRemoteDataSource.setDailyStudyReminderNotificationHour(profileId, notificationHour, timeZone)

    override suspend fun disableDailyStudyReminderNotification(profileId: Long): Result<Profile> =
        profileRemoteDataSource.setDailyStudyReminderNotificationHour(profileId, null)

    override suspend fun setTimeZone(profileId: Long, timeZone: TimeZone): Result<Profile> =
        profileRemoteDataSource.setTimeZone(profileId, timeZone)
}