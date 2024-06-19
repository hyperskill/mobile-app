package org.hyperskill.app.legacy_welcome_onboarding.injection

import org.hyperskill.app.legacy_welcome_onboarding.presentation.LegacyWelcomeOnboardingActionDispatcher
import org.hyperskill.app.legacy_welcome_onboarding.presentation.LegacyWelcomeOnboardingReducer

@Deprecated("Should be removed in ALTAPPS-1276")
interface LegacyWelcomeOnboardingComponent {
    val legacyWelcomeOnboardingReducer: LegacyWelcomeOnboardingReducer
    val legacyWelcomeOnboardingActionDispatcher: LegacyWelcomeOnboardingActionDispatcher
}