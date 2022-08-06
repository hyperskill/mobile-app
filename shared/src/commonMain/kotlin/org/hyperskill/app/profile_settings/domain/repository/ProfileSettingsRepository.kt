package org.hyperskill.app.profile_settings.domain.repository

import org.hyperskill.app.profile_settings.domain.model.ProfileSettings
import org.hyperskill.app.profile_settings.domain.model.Theme

interface ProfileSettingsRepository {
    fun getProfileSettings(): Result<ProfileSettings>

    fun saveProfileSettings(profileSettings: ProfileSettings)

    fun changeTheme(theme: Theme)
}