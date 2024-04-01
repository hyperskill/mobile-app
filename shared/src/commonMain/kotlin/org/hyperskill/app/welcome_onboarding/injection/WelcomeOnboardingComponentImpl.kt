package org.hyperskill.app.welcome_onboarding.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingActionDispatcher
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingReducer

internal class WelcomeOnboardingComponentImpl(
    private val appGraph: AppGraph
) : WelcomeOnboardingComponent {
    override val welcomeOnboardingReducer: WelcomeOnboardingReducer
        get() = WelcomeOnboardingReducer()

    override val welcomeOnboardingActionDispatcher: WelcomeOnboardingActionDispatcher
        get() = WelcomeOnboardingActionDispatcher(
            config = ActionDispatcherOptions(),
            onboardingInteractor = appGraph.buildOnboardingDataComponent().onboardingInteractor
        )
}