package org.hyperskill.app.android.profile_settings.view.mapper

import androidx.appcompat.app.AppCompatDelegate
import org.hyperskill.app.profile_settings.domain.model.Theme

object ThemeMapper {
    fun getAppCompatDelegate(theme: Theme): Int =
        when (theme) {
            Theme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            Theme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            Theme.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
}