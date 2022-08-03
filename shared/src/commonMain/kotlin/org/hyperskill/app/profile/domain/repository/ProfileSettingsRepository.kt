package org.hyperskill.app.profile.domain.repository

import org.hyperskill.app.profile.domain.model.ProfileSettings
import org.hyperskill.app.profile.domain.model.Theme

interface ProfileSettingsRepository {
    suspend fun getProfileSettings(): Result<ProfileSettings>

    suspend fun saveProfileSettings(profileSettings: ProfileSettings)

    suspend fun changeTheme(theme: Theme)
}