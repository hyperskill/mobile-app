package org.hyperskill.app.welcome.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

interface PlatformWelcomeComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}