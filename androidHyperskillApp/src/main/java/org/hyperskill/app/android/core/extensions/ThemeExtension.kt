package org.hyperskill.app.android.core.extensions

import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.profile_settings.domain.model.Theme

val Theme.representation
    get() = when (this) {
        Theme.DARK ->
            HyperskillApp.graph().context.resources.getString(org.hyperskill.app.R.string.settings_theme_dark)
        Theme.LIGHT ->
            HyperskillApp.graph().context.resources.getString(org.hyperskill.app.R.string.settings_theme_light)
        Theme.SYSTEM ->
            HyperskillApp.graph().context.resources.getString(org.hyperskill.app.R.string.settings_theme_system)
    }