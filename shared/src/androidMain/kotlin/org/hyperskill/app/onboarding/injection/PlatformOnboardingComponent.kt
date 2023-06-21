package org.hyperskill.app.onboarding.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

interface PlatformOnboardingComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}