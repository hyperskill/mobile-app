package org.hyperskill.app.interview_preparation_onboarding.presentation

import org.hyperskill.app.interview_preparation_onboarding.domain.analytic.InterviewPreparationOnboardingGoToProblemClickedAnalyticsEvent
import org.hyperskill.app.interview_preparation_onboarding.domain.analytic.InterviewPreparationOnboardingViewedAnalyticsEvent
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingFeature.Action
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingFeature.InternalAction
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingFeature.Message
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias InterviewPreparationOnboardingReducerResult = Pair<State, Set<Action>>

internal class InterviewPreparationOnboardingReducer : StateReducer<State, Message, Action> {
    override fun reduce(
        state: State,
        message: Message
    ): InterviewPreparationOnboardingReducerResult =
        when (message) {
            Message.ViewedEventMessage ->
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        InterviewPreparationOnboardingViewedAnalyticsEvent
                    ),
                    InternalAction.MarkOnboardingAsViewed
                )
            Message.GoToFirstProblemClicked ->
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        InterviewPreparationOnboardingGoToProblemClickedAnalyticsEvent
                    ),
                    Action.ViewAction.NavigateTo.StepScreen(state.stepRoute)
                )
        }
}