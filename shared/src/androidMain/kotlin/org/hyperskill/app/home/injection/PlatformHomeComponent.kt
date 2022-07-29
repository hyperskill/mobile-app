package org.hyperskill.app.home.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

actual interface PlatformHomeComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}