package org.hyperskill.app.profile.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

actual interface PlatformProfileComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}