package org.hyperskill.app.profile.domain.repository

import kotlinx.datetime.TimeZone
import org.hyperskill.app.profile.domain.model.Profile

interface ProfileRepository {
    suspend fun selectTrackWithProject(profileId: Long, trackId: Long, projectId: Long): Result<Profile>
    suspend fun selectTrack(profileId: Long, trackId: Long): Result<Profile>
    suspend fun setTimeZone(profileId: Long, timeZone: TimeZone): Result<Profile>
}