package org.hyperskill.app.auth.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

actual interface PlatformAuthSocialComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}