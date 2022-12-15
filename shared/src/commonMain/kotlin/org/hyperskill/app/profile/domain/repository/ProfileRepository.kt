package org.hyperskill.app.profile.domain.repository

import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.profile.domain.model.Profile

interface ProfileRepository {
    suspend fun getCurrentProfile(primarySourceType: DataSourceType = DataSourceType.CACHE): Result<Profile>
    suspend fun selectTrackWithProject(profileId: Long, trackId: Long, projectId: Long): Result<Profile>
    suspend fun clearCache()
}