package org.hyperskill.app.interview_preparation_onboarding.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingFeature.Action
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingFeature.Message
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingFeature.State
import org.hyperskill.app.step.domain.model.StepRoute
import ru.nobird.app.presentation.redux.feature.Feature

internal class InterviewPreparationOnboardingComponentImpl(
    private val appGraph: AppGraph
) : InterviewPreparationOnboardingComponent {
    override fun interviewPreparationOnboardingFeature(stepRoute: StepRoute): Feature<State, Message, Action> =
        InterviewPreparationOnboardingFeatureBuilder.build(
            stepRoute = stepRoute,
            analyticsInteractor = appGraph.analyticComponent.analyticInteractor,
            onboardingInteractor = appGraph.buildOnboardingDataComponent().onboardingInteractor,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant
        )
}