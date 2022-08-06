package org.hyperskill.app.profile.domain.repository

import org.hyperskill.app.profile.domain.model.ProfileSettings
import org.hyperskill.app.profile.domain.model.Theme

interface ProfileSettingsRepository {
    fun getProfileSettings(): Result<ProfileSettings>

    fun saveProfileSettings(profileSettings: ProfileSettings)

    fun changeTheme(theme: Theme)
}