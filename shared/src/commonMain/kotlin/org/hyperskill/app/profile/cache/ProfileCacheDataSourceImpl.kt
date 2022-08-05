package org.hyperskill.app.profile.cache

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hyperskill.app.profile.data.source.ProfileCacheDataSource
import org.hyperskill.app.profile.domain.model.Profile

class ProfileCacheDataSourceImpl(
    private val json: Json,
    private val settings: Settings
) : ProfileCacheDataSource {
    override suspend fun getCurrentProfile(): Result<Profile> =
        kotlin.runCatching {
            val key =
                when {
                    settings.hasKey(ProfileCacheKeyValues.CURRENT_PROFILE) ->
                        ProfileCacheKeyValues.CURRENT_PROFILE
                    settings.hasKey(ProfileCacheKeyValues.GUEST_PROFILE) ->
                        ProfileCacheKeyValues.GUEST_PROFILE
                    else ->
                        throw IllegalArgumentException("Profile information is not cached")
                }
            json.decodeFromString(settings[key, ""])
        }

    override suspend fun saveProfile(profile: Profile) {
        val key =
            if (profile.isGuest) {
                ProfileCacheKeyValues.GUEST_PROFILE
            } else {
                ProfileCacheKeyValues.CURRENT_PROFILE
            }
        settings.putString(key, json.encodeToString(profile))
    }

    override suspend fun clearCache() {
        kotlin.runCatching {
            settings.remove(ProfileCacheKeyValues.GUEST_PROFILE)
            settings.remove(ProfileCacheKeyValues.CURRENT_PROFILE)
        }
    }
}