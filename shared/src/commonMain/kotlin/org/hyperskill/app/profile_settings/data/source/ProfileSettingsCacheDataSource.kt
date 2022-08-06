package org.hyperskill.app.profile_settings.data.source

import org.hyperskill.app.profile_settings.domain.model.ProfileSettings
import org.hyperskill.app.profile_settings.domain.model.Theme

interface ProfileSettingsCacheDataSource {
    fun getProfileSettings(): Result<ProfileSettings>
    fun saveProfileSettings(profileSettings: ProfileSettings)
    fun changeTheme(theme: Theme)
}