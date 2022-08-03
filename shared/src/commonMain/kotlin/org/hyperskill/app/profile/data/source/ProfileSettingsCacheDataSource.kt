package org.hyperskill.app.profile.data.source

import org.hyperskill.app.profile.domain.model.ProfileSettings

interface ProfileSettingsCacheDataSource {
    suspend fun getProfileSettings(): Result<ProfileSettings>
    suspend fun saveProfileSettings(profileSettings: ProfileSettings)
}