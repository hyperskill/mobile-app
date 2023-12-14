package org.hyperskill.app.welcome_onboarding.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingDispatcher
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingReducer

class WelcomeOnboardingComponentImpl(
    private val appGraph: AppGraph
) : WelcomeOnboardingComponent {
    override val welcomeOnboardingReducer: WelcomeOnboardingReducer
        get() = WelcomeOnboardingReducer()
    override val welcomeOnboardingDispatcher: WelcomeOnboardingDispatcher
        get() = WelcomeOnboardingDispatcher(
            config = ActionDispatcherOptions(),
            onboardingInteractor = appGraph.buildOnboardingDataComponent().onboardingInteractor
        )
}