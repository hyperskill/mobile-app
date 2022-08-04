package org.hyperskill.app.profile.data.source

import org.hyperskill.app.profile.domain.model.ProfileSettings
import org.hyperskill.app.profile.domain.model.Theme

interface ProfileSettingsCacheDataSource {
    fun getProfileSettings(): Result<ProfileSettings>
    fun saveProfileSettings(profileSettings: ProfileSettings)
    fun changeTheme(theme: Theme)
}