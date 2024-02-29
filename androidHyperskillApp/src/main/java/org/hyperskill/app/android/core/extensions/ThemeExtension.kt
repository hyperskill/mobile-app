package org.hyperskill.app.android.core.extensions

import android.content.Context
import org.hyperskill.app.profile_settings.domain.model.Theme

fun Theme.getStringRepresentation(context: Context): String =
    when (this) {
        Theme.DARK ->
            context.resources.getString(org.hyperskill.app.R.string.settings_theme_dark)
        Theme.LIGHT ->
            context.resources.getString(org.hyperskill.app.R.string.settings_theme_light)
        Theme.SYSTEM ->
            context.resources.getString(org.hyperskill.app.R.string.settings_theme_system)
    }