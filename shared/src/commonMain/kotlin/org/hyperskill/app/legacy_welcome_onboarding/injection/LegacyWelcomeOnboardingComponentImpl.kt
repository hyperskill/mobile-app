package org.hyperskill.app.legacy_welcome_onboarding.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.legacy_welcome_onboarding.presentation.LegacyWelcomeOnboardingActionDispatcher
import org.hyperskill.app.legacy_welcome_onboarding.presentation.LegacyWelcomeOnboardingReducer

internal class LegacyWelcomeOnboardingComponentImpl(
    private val appGraph: AppGraph
) : LegacyWelcomeOnboardingComponent {
    override val legacyWelcomeOnboardingReducer: LegacyWelcomeOnboardingReducer
        get() = LegacyWelcomeOnboardingReducer()

    override val legacyWelcomeOnboardingActionDispatcher: LegacyWelcomeOnboardingActionDispatcher
        get() = LegacyWelcomeOnboardingActionDispatcher(
            config = ActionDispatcherOptions(),
            onboardingInteractor = appGraph.buildOnboardingDataComponent().onboardingInteractor
        )
}