package org.hyperskill.app.welcome_onboarding.injection

import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingDispatcher
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingReducer

interface WelcomeOnboardingComponent {
    val welcomeOnboardingReducer: WelcomeOnboardingReducer
    val welcomeOnboardingDispatcher: WelcomeOnboardingDispatcher
}