package org.hyperskill.app.profile.domain.interactor

import org.hyperskill.app.profile.domain.model.ProfileSettings
import org.hyperskill.app.profile.domain.model.Theme
import org.hyperskill.app.profile.domain.repository.ProfileSettingsRepository

class ProfileSettingsInteractor(
    private val profileSettingsRepository: ProfileSettingsRepository
) {
    suspend fun getProfileSettings(): Result<ProfileSettings> =
        profileSettingsRepository.getProfileSettings()

    suspend fun saveProfileSettings(profileSettings: ProfileSettings) {
        profileSettingsRepository.saveProfileSettings(profileSettings)
    }

    suspend fun changeTheme(theme: Theme) {
        profileSettingsRepository.changeTheme(theme)
    }
}