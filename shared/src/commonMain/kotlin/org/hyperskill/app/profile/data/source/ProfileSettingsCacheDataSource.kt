package org.hyperskill.app.profile.data.source

import org.hyperskill.app.profile.domain.model.ProfileSettings
import org.hyperskill.app.profile.domain.model.Theme

interface ProfileSettingsCacheDataSource {
    suspend fun getProfileSettings(): Result<ProfileSettings>
    suspend fun saveProfileSettings(profileSettings: ProfileSettings)
    suspend fun changeTheme(theme: Theme)
}