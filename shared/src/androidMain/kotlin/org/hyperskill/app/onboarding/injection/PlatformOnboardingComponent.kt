package org.hyperskill.app.onboarding.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

actual interface PlatformOnboardingComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}