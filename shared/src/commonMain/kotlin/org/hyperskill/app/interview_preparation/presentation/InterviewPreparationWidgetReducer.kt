package org.hyperskill.app.interview_preparation.presentation

import org.hyperskill.app.interview_preparation.domain.analytic.InterviewPreparationWidgetClickedHyperskillAnalyticEvent
import org.hyperskill.app.interview_preparation.domain.analytic.InterviewPreparationWidgetClickedRetryContentLoadingHyperskillAnalyticEvent
import org.hyperskill.app.interview_preparation.domain.analytic.InterviewPreparationWidgetViewedHyperskillAnalyticEvent
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetFeature.Action
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetFeature.InternalAction
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetFeature.InternalMessage
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetFeature.Message
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetFeature.State
import org.hyperskill.app.step.domain.model.StepRoute
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias InterviewPreparationWidgetReducerResult = Pair<State, Set<Action>>

class InterviewPreparationWidgetReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): InterviewPreparationWidgetReducerResult =
        when (message) {
            is InternalMessage.Initialize ->
                handleInitializeMessage(state, message)
            is InternalMessage.FetchMobileInterviewPreparationFeatureFlagResult ->
                handleFetchMobileInterviewPreparationFeatureFlagResultMessage(state, message)
            is InternalMessage.FetchInterviewStepsResultSuccess ->
                State.Content(stepsCount = message.steps.count()) to emptySet()
            is InternalMessage.FetchInterviewStepsResultError ->
                State.Error to emptySet()
            InternalMessage.PullToRefresh ->
                handlePullToRefreshMessage(state)
            Message.RetryContentLoading ->
                handleRetryContentLoadingMessage(state)
            Message.ViewedEventMessage ->
                handleWidgetViewedMessage(state)
            Message.WidgetClicked ->
                handleWidgetClickedMessage(state)
            is InternalMessage.StepsCountChanged ->
                handleStepCountChangedMessage(state, message)
            is InternalMessage.FetchNextInterviewStepResultSuccess ->
                handleFetchNextInterviewStepResultSuccessMessage(state, message)
            is InternalMessage.FetchNextInterviewStepResultError ->
                state to setOf(Action.ViewAction.ShowOpenStepError(message.errorMessage))
        }

    private fun handleInitializeMessage(
        state: State,
        message: InternalMessage.Initialize
    ): InterviewPreparationWidgetReducerResult =
        state to setOf(
            InternalAction.FetchMobileInterviewPreparationFeatureFlag(
                originalInitializeForceUpdate = message.forceUpdate
            )
        )

    private fun handleFetchMobileInterviewPreparationFeatureFlagResultMessage(
        state: State,
        message: InternalMessage.FetchMobileInterviewPreparationFeatureFlagResult
    ): InterviewPreparationWidgetReducerResult =
        if (message.isMobileInterviewPreparationEnabled) {
            when (state) {
                State.Idle ->
                    State.Loading(isLoadingSilently = false) to
                        setOf(InternalAction.FetchInterviewSteps(false))
                State.Error ->
                    if (message.originalInitializeForceUpdate) {
                        State.Loading(isLoadingSilently = false) to
                            setOf(InternalAction.FetchInterviewSteps(true))
                    } else {
                        state to emptySet()
                    }
                is State.Content ->
                    if (message.originalInitializeForceUpdate) {
                        State.Loading(
                            isLoadingSilently = state.stepsCount == 0
                        ) to setOf(InternalAction.FetchInterviewSteps(true))
                    } else {
                        state to emptySet()
                    }
                is State.Loading ->
                    state to emptySet()
            }
        } else {
            State.Idle to emptySet()
        }

    private fun handlePullToRefreshMessage(
        state: State
    ): InterviewPreparationWidgetReducerResult =
        when (state) {
            is State.Content ->
                if (state.isRefreshing) {
                    state to emptySet()
                } else {
                    state.copy(isRefreshing = true) to
                        setOf(InternalAction.FetchInterviewSteps(true))
                }
            is State.Error ->
                State.Loading(isLoadingSilently = false) to
                    setOf(InternalAction.FetchInterviewSteps(true))
            else ->
                state to emptySet()
        }

    private fun handleRetryContentLoadingMessage(
        state: State
    ): InterviewPreparationWidgetReducerResult =
        if (state is State.Error) {
            State.Loading(isLoadingSilently = false) to setOf(
                InternalAction.FetchInterviewSteps(true),
                InternalAction.LogAnalyticEvent(
                    InterviewPreparationWidgetClickedRetryContentLoadingHyperskillAnalyticEvent
                )
            )
        } else {
            state to emptySet()
        }

    private fun handleWidgetClickedMessage(
        state: State
    ): InterviewPreparationWidgetReducerResult =
        if (state is State.Content) {
            state to setOf(
                InternalAction.LogAnalyticEvent(
                    InterviewPreparationWidgetClickedHyperskillAnalyticEvent
                ),
                InternalAction.FetchNextInterviewStep
            )
        } else {
            state to emptySet()
        }

    private fun handleFetchNextInterviewStepResultSuccessMessage(
        state: State,
        message: InternalMessage.FetchNextInterviewStepResultSuccess
    ): InterviewPreparationWidgetReducerResult {
        val stepRoute = StepRoute.InterviewPreparation(message.stepId)
        return state to setOf(
            if (!message.wasOnboardingShown) {
                Action.ViewAction.NavigateTo.InterviewPreparationOnboarding(stepRoute)
            } else {
                Action.ViewAction.NavigateTo.Step(stepRoute)
            }
        )
    }

    private fun handleWidgetViewedMessage(
        state: State
    ): InterviewPreparationWidgetReducerResult =
        if (state !is State.Idle) {
            state to setOf(
                InternalAction.LogAnalyticEvent(
                    InterviewPreparationWidgetViewedHyperskillAnalyticEvent
                )
            )
        } else {
            state to emptySet()
        }

    private fun handleStepCountChangedMessage(
        state: State,
        message: InternalMessage.StepsCountChanged
    ): InterviewPreparationWidgetReducerResult =
        if (state is State.Content) {
            state.copy(stepsCount = message.stepsCount) to emptySet()
        } else {
            state to emptySet()
        }
}