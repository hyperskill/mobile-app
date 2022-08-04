package org.hyperskill.app.profile.domain.interactor

import org.hyperskill.app.profile.domain.model.ProfileSettings
import org.hyperskill.app.profile.domain.model.Theme
import org.hyperskill.app.profile.domain.repository.ProfileSettingsRepository

class ProfileSettingsInteractor(
    private val profileSettingsRepository: ProfileSettingsRepository
) {
    fun getProfileSettings(): Result<ProfileSettings> =
        profileSettingsRepository.getProfileSettings()

    fun saveProfileSettings(profileSettings: ProfileSettings) {
        profileSettingsRepository.saveProfileSettings(profileSettings)
    }

    fun changeTheme(theme: Theme) {
        profileSettingsRepository.changeTheme(theme)
    }
}