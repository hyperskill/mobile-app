package org.hyperskill.app.profile.data.source

import org.hyperskill.app.profile.domain.model.Profile

interface ProfileRemoteDataSource {
    suspend fun getCurrentProfile(): Result<Profile>

    suspend fun selectTrackWithProject(profileId: Long, trackId: Long, projectId: Long): Result<Profile>

    suspend fun selectTrack(profileId: Long, trackId: Long): Result<Profile>
}