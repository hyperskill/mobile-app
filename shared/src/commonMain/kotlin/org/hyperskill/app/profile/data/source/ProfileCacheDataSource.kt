package org.hyperskill.app.profile.data.source

import org.hyperskill.app.profile.domain.model.Profile

interface ProfileCacheDataSource {
    suspend fun getCurrentProfile(): Result<Profile>
    suspend fun saveProfile(profile: Profile)
    suspend fun clearCache()
}