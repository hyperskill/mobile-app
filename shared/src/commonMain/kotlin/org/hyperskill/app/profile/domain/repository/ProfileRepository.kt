package org.hyperskill.app.profile.domain.repository

import org.hyperskill.app.profile.domain.model.Profile

interface ProfileRepository : CurrentProfileStateRepository {
    suspend fun selectTrackWithProject(profileId: Long, trackId: Long, projectId: Long): Result<Profile>
    suspend fun selectTrack(profileId: Long, trackId: Long): Result<Profile>
}