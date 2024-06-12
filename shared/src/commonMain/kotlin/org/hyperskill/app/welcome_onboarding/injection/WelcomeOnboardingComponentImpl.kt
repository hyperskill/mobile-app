package org.hyperskill.app.welcome_onboarding.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Action
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Message
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.State
import ru.nobird.app.presentation.redux.feature.Feature

internal class WelcomeOnboardingComponentImpl(
    private val appGraph: AppGraph
) : WelcomeOnboardingComponent {
    override val welcomeOnboardingFeature: Feature<State, Message, Action>
        get() = WelcomeOnboardingFeatureBuilder.build(
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant
        )
}