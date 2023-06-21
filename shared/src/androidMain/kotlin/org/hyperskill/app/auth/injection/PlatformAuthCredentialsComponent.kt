package org.hyperskill.app.auth.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

interface PlatformAuthCredentialsComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}