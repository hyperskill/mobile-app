package org.hyperskill.app.welcome_onboarding.injection

import org.hyperskill.app.core.domain.platform.PlatformType
import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingActionDispatcher
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingReducer

internal class WelcomeOnboardingComponentImpl(
    private val appGraph: AppGraph
) : WelcomeOnboardingComponent {
    override val welcomeOnboardingReducer: WelcomeOnboardingReducer
        get() = WelcomeOnboardingReducer(
            isSubscriptionPurchaseEnabled = appGraph.commonComponent.platform.isSubscriptionPurchaseEnabled,
            isUsersQuestionnaireOnboardingEnabled = appGraph.commonComponent.platform.platformType == PlatformType.IOS
        )

    override val welcomeOnboardingActionDispatcher: WelcomeOnboardingActionDispatcher
        get() = WelcomeOnboardingActionDispatcher(
            config = ActionDispatcherOptions(),
            onboardingInteractor = appGraph.buildOnboardingDataComponent().onboardingInteractor,
            currentSubscriptionStateRepository = appGraph.stateRepositoriesComponent.currentSubscriptionStateRepository
        )
}