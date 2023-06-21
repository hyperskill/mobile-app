package org.hyperskill.app.auth.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

interface PlatformAuthSocialWebViewComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}