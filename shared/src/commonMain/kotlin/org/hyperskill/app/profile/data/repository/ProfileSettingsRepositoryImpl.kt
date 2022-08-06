package org.hyperskill.app.profile.data.repository

import org.hyperskill.app.profile.data.source.ProfileSettingsCacheDataSource
import org.hyperskill.app.profile.domain.model.ProfileSettings
import org.hyperskill.app.profile.domain.model.Theme
import org.hyperskill.app.profile.domain.repository.ProfileSettingsRepository

class ProfileSettingsRepositoryImpl(
    private val profileSettingsCacheDataSource: ProfileSettingsCacheDataSource
) : ProfileSettingsRepository {
    override fun getProfileSettings(): Result<ProfileSettings> =
        profileSettingsCacheDataSource.getProfileSettings()

    override fun saveProfileSettings(profileSettings: ProfileSettings) {
        profileSettingsCacheDataSource.saveProfileSettings(profileSettings)
    }

    override fun changeTheme(theme: Theme) {
        profileSettingsCacheDataSource.changeTheme(theme)
    }
}