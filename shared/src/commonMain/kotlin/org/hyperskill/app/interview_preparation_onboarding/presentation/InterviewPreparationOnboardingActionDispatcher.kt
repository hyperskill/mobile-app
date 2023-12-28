package org.hyperskill.app.interview_preparation_onboarding.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingFeature.Action
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingFeature.Message
import org.hyperskill.app.onboarding.domain.interactor.OnboardingInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class InterviewPreparationOnboardingActionDispatcher(
    config: ActionDispatcherOptions,
    private val analyticInteractor: AnalyticInteractor,
    private val onboardingInteractor: OnboardingInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InterviewPreparationOnboardingFeature.InternalAction.LogAnalyticsEvent ->
                analyticInteractor.logEvent(action.event)
            InterviewPreparationOnboardingFeature.InternalAction.MarkOnboardingAsViewed ->
                onboardingInteractor.setInterviewPreparationOnboardingWasShown(true)
            else -> {
                // no op
            }
        }
    }
}