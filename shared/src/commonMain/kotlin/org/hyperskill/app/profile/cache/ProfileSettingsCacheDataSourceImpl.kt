package org.hyperskill.app.profile.cache

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hyperskill.app.profile.data.source.ProfileSettingsCacheDataSource
import org.hyperskill.app.profile.domain.model.ProfileSettings
import org.hyperskill.app.profile.domain.model.Theme

class ProfileSettingsCacheDataSourceImpl(
    private val json: Json,
    private val settings: Settings
) : ProfileSettingsCacheDataSource {

    override fun getProfileSettings(): Result<ProfileSettings> =
        json.decodeFromString(settings[ProfileSettingsCacheKeyValues.PROFILE_SETTINGS, ""])

    override fun saveProfileSettings(profileSettings: ProfileSettings) {
        settings.putString(
            ProfileSettingsCacheKeyValues.PROFILE_SETTINGS,
            json.encodeToString(profileSettings)
        )
    }

    override fun changeTheme(theme: Theme) {
        saveProfileSettings(
            getProfileSettings()
                .getOrDefault(defaultSettings())
                .copy(theme = theme)
        )
    }

    private fun defaultSettings(): ProfileSettings =
        ProfileSettings(Theme.SYSTEM)
}