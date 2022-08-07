package org.hyperskill.app.profile_settings.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

actual interface PlatformProfileSettingsComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}