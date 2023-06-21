package org.hyperskill.app.debug.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

interface PlatformDebugComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}