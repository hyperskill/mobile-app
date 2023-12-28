package org.hyperskill.app.interview_preparation_onboarding.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingActionDispatcher
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingFeature
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingFeature.Action
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingFeature.Message
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingReducer
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.onboarding.domain.interactor.OnboardingInteractor
import org.hyperskill.app.step.domain.model.StepRoute
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object InterviewPreparationOnboardingFeatureBuilder {
    private const val LOG_TAG = "InterviewPreparationOnboardingFeature"

    fun build(
        stepRoute: StepRoute,
        analyticsInteractor: AnalyticInteractor,
        onboardingInteractor: OnboardingInteractor,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<InterviewPreparationOnboardingFeature.State, Message, Action> {
        val reducer =
            InterviewPreparationOnboardingReducer()
                .wrapWithLogger(buildVariant, logger, LOG_TAG)
        val actionDispatcher =
            InterviewPreparationOnboardingActionDispatcher(
                config = ActionDispatcherOptions(),
                analyticInteractor = analyticsInteractor,
                onboardingInteractor = onboardingInteractor
            )
        return ReduxFeature(
            InterviewPreparationOnboardingFeature.initialState(stepRoute),
            reducer
        ).wrapWithActionDispatcher(actionDispatcher)
    }
}