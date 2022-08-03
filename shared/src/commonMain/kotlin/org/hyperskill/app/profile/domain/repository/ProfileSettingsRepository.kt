package org.hyperskill.app.profile.domain.repository

import org.hyperskill.app.profile.domain.model.ProfileSettings

interface ProfileSettingsRepository {
    suspend fun getProfileSettings(): Result<ProfileSettings>

    suspend fun saveProfileSettings(profileSettings: ProfileSettings)
}