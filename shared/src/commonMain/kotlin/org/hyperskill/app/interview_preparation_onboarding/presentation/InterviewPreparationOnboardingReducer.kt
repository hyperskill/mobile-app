package org.hyperskill.app.interview_preparation_onboarding.presentation

import org.hyperskill.app.interview_preparation_onboarding.domain.analytics.InterviewPreparationOnboardingGoToProblemClickedAnalyticsEvent
import org.hyperskill.app.interview_preparation_onboarding.domain.analytics.InterviewPreparationOnboardingViewedAnalyticsEvent
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingFeature.Action
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingFeature.InternalAction
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingFeature.Message
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias InterviewPreparationOnboardingReducerResult = Pair<State, Set<Action>>

class InterviewPreparationOnboardingReducer : StateReducer<State, Message, Action> {
    override fun reduce(
        state: State,
        message: Message
    ): InterviewPreparationOnboardingReducerResult =
        when (message) {
            Message.ViewedEventMessage ->
                state to setOf(
                    InternalAction.LogAnalyticsEvent(
                        InterviewPreparationOnboardingViewedAnalyticsEvent
                    ),
                    InternalAction.MarkOnboardingAsViewed
                )
            Message.GoToFirstProblemClicked ->
                state to setOf(
                    InternalAction.LogAnalyticsEvent(
                        InterviewPreparationOnboardingGoToProblemClickedAnalyticsEvent
                    ),
                    Action.ViewAction.NavigateTo.StepScreen(state.stepRoute)
                )
        }
}