package org.hyperskill.app.first_problem_onboarding.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.Action
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.Message
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

internal class FirstProblemOnboardingComponentImpl(
    private val appGraph: AppGraph
) : FirstProblemOnboardingComponent {
    override fun firstProblemOnboardingFeature(isNewUserMode: Boolean): Feature<ViewState, Message, Action> =
        FirstProblemOnboardingFeatureBuilder.build(
            isNewUserMode = isNewUserMode,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            currentProfileStateRepository = appGraph.profileDataComponent.currentProfileStateRepository,
            learningActivitiesRepository = appGraph.buildLearningActivitiesDataComponent().learningActivitiesRepository,
            resourceProvider = appGraph.commonComponent.resourceProvider,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant
        )
}