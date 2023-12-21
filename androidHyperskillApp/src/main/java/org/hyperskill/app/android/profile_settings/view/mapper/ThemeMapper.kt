package org.hyperskill.app.android.profile_settings.view.mapper

import androidx.appcompat.app.AppCompatDelegate
import org.hyperskill.app.profile_settings.domain.model.Theme

fun Theme.asNightMode(): Int =
    when (this) {
        Theme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
        Theme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
        Theme.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }