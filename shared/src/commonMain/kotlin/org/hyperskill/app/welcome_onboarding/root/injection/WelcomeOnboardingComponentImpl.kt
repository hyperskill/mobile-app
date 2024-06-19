package org.hyperskill.app.welcome_onboarding.root.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingFeatureParams
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingFeature.Action
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingFeature.Message
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingFeature.State
import ru.nobird.app.presentation.redux.feature.Feature

internal class WelcomeOnboardingComponentImpl(
    private val appGraph: AppGraph,
    private val params: WelcomeOnboardingFeatureParams
) : WelcomeOnboardingComponent {
    override val welcomeOnboardingFeature: Feature<State, Message, Action>
        get() = WelcomeOnboardingFeatureBuilder.build(
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor,
            learningActivityRepository = appGraph.buildLearningActivitiesDataComponent().learningActivitiesRepository,
            params = params
        )
}