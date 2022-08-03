package org.hyperskill.app.profile.cache

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hyperskill.app.profile.data.source.ProfileSettingsCacheDataSource
import org.hyperskill.app.profile.domain.model.ProfileSettings

class ProfileSettingsCacheDataSourceImpl(
    private val json: Json,
    private val settings: Settings
) : ProfileSettingsCacheDataSource {

    override suspend fun getProfileSettings(): Result<ProfileSettings> =
        json.decodeFromString(settings[ProfileSettingsCacheKeyValues.PROFILE_SETTINGS, ""])

    override suspend fun saveProfileSettings(profileSettings: ProfileSettings): Unit =
        settings.putString(
            ProfileSettingsCacheKeyValues.PROFILE_SETTINGS,
            json.encodeToString(profileSettings)
        )
}