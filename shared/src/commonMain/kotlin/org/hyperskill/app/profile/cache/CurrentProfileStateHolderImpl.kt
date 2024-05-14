package org.hyperskill.app.profile.cache

import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json
import org.hyperskill.app.profile.data.source.CurrentProfileStateHolder
import org.hyperskill.app.profile.domain.model.Profile

class CurrentProfileStateHolderImpl(
    private val json: Json,
    private val settings: Settings
) : CurrentProfileStateHolder {

    private var cachedProfile: Profile? = null

    override suspend fun getState(): Profile? {
        if (cachedProfile == null) {
            cachedProfile = readProfileFromSettings()
        }
        return cachedProfile
    }

    private fun readProfileFromSettings(): Profile? {
        val key =
            when {
                settings.hasKey(ProfileCacheKeyValues.CURRENT_PROFILE) ->
                    ProfileCacheKeyValues.CURRENT_PROFILE
                settings.hasKey(ProfileCacheKeyValues.GUEST_PROFILE) ->
                    ProfileCacheKeyValues.GUEST_PROFILE
                else -> {
                    return null
                }
            }
        return json.decodeFromString(
            Profile.serializer(),
            settings.getString(key = key, defaultValue = "")
        )
    }

    override suspend fun setState(newState: Profile) {
        this.cachedProfile = newState
        val key =
            if (newState.isGuest) {
                ProfileCacheKeyValues.GUEST_PROFILE
            } else {
                ProfileCacheKeyValues.CURRENT_PROFILE
            }
        settings.putString(
            key = key,
            value = json.encodeToString(Profile.serializer(), newState)
        )
    }

    override fun resetState() {
        this.cachedProfile = null
        settings.remove(ProfileCacheKeyValues.GUEST_PROFILE)
        settings.remove(ProfileCacheKeyValues.CURRENT_PROFILE)
    }
}