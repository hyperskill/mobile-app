package org.hyperskill.app.profile_settings.cache

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hyperskill.app.profile_settings.data.source.ProfileSettingsCacheDataSource
import org.hyperskill.app.profile_settings.domain.model.ProfileSettings
import org.hyperskill.app.profile_settings.domain.model.Theme

class ProfileSettingsCacheDataSourceImpl(
    private val json: Json,
    private val settings: Settings
) : ProfileSettingsCacheDataSource {

    override fun getProfileSettings(): Result<ProfileSettings> {
        val jsonValue: String? = settings.get(ProfileSettingsCacheKeyValues.PROFILE_SETTINGS)
        return if (jsonValue != null) {
            json.decodeFromString(jsonValue)
        } else {
            val defaultSettings = createDefaultSettings()
            saveProfileSettings(defaultSettings)
            Result.success(defaultSettings)
        }
    }

    override fun saveProfileSettings(profileSettings: ProfileSettings) {
        settings.putString(
            ProfileSettingsCacheKeyValues.PROFILE_SETTINGS,
            json.encodeToString(profileSettings)
        )
    }

    override fun changeTheme(theme: Theme) {
        saveProfileSettings(
            getProfileSettings()
                .getOrDefault(createDefaultSettings())
                .copy(theme = theme)
        )
    }

    private fun createDefaultSettings(): ProfileSettings =
        ProfileSettings()
}