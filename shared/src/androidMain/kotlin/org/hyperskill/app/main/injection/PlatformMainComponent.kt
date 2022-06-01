package org.hyperskill.app.main.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

actual interface PlatformMainComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}