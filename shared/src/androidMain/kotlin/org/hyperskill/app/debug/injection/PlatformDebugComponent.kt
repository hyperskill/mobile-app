package org.hyperskill.app.debug.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

actual interface PlatformDebugComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}