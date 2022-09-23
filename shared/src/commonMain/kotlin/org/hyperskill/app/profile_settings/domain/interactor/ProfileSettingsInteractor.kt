package org.hyperskill.app.profile_settings.domain.interactor

import org.hyperskill.app.profile_settings.domain.model.ProfileSettings
import org.hyperskill.app.profile_settings.domain.model.Theme
import org.hyperskill.app.profile_settings.domain.repository.ProfileSettingsRepository

class ProfileSettingsInteractor(
    private val profileSettingsRepository: ProfileSettingsRepository
) {
    fun getProfileSettings(): ProfileSettings =
        profileSettingsRepository.getProfileSettings()

    fun saveProfileSettings(profileSettings: ProfileSettings) {
        profileSettingsRepository.saveProfileSettings(profileSettings)
    }

    fun changeTheme(theme: Theme) {
        profileSettingsRepository.changeTheme(theme)
    }
}