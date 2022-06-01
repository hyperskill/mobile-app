package org.hyperskill.app.auth.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

actual interface PlatformAuthCredentialsComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}