package org.hyperskill.app.interview_preparation.presentation

import org.hyperskill.app.interview_preparation.domain.analytics.InterviewPreparationWidgetClickedHyperskillAnalyticsEvent
import org.hyperskill.app.interview_preparation.domain.analytics.InterviewPreparationWidgetClickedRetryContentLoadingHyperskillAnalyticsEvent
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetFeature.Action
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetFeature.InternalAction
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetFeature.InternalMessage
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetFeature.Message
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetFeature.State
import org.hyperskill.app.step.domain.model.StepRoute
import ru.nobird.app.core.model.mutate
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias InterviewPreparationWidgetReducerResult = Pair<State, Set<Action>>

class InterviewPreparationWidgetReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): InterviewPreparationWidgetReducerResult =
        when (message) {
            is InternalMessage.Initialize -> handleInitializeMessage(state, message)
            is InternalMessage.FetchInterviewStepsResultSuccess -> {
                State.Content(steps = message.steps) to emptySet()
            }
            is InternalMessage.FetchInterviewStepsResultError -> {
                State.Error to emptySet()
            }
            InternalMessage.PullToRefresh -> handlePullToRefresh(state)
            Message.RetryContentLoading -> handleRetryContentLoading(state)
            Message.WidgetClicked -> handleWidgetClicked(state)
            is InternalMessage.StepSolved -> handleStepSolved(state, message)
            is InternalMessage.OnboardingFlagFetchResult -> handleOnboardingFlagFetchResult(state, message)
        }

    private fun handleInitializeMessage(
        state: State,
        message: InternalMessage.Initialize
    ): InterviewPreparationWidgetReducerResult =
        when (state) {
            State.Idle ->
                State.Loading(isLoadingSilently = false) to setOf(InternalAction.FetchInterviewSteps)
            State.Error ->
                if (message.forceUpdate) {
                    State.Loading(isLoadingSilently = false) to setOf(InternalAction.FetchInterviewSteps)
                } else {
                    state to emptySet()
                }
            is State.Content ->
                if (message.forceUpdate) {
                    State.Loading(
                        isLoadingSilently = state.steps.isEmpty()
                    ) to setOf(InternalAction.FetchInterviewSteps)
                } else {
                    state to emptySet()
                }
            is State.Loading -> state to emptySet()
        }

    private fun handlePullToRefresh(
        state: State
    ): InterviewPreparationWidgetReducerResult =
        when (state) {
            is State.Content ->
                if (state.isRefreshing) {
                    state to emptySet()
                } else {
                    state.copy(isRefreshing = true) to setOf(InternalAction.FetchInterviewSteps)
                }
            is State.Error ->
                State.Loading(isLoadingSilently = false) to setOf(InternalAction.FetchInterviewSteps)
            else ->
                state to emptySet()
        }

    private fun handleRetryContentLoading(
        state: State
    ): InterviewPreparationWidgetReducerResult =
        if (state is State.Error) {
            State.Loading(isLoadingSilently = false) to setOf(
                InternalAction.FetchInterviewSteps,
                InternalAction.LogAnalyticEvent(
                    InterviewPreparationWidgetClickedRetryContentLoadingHyperskillAnalyticsEvent
                )
            )
        } else {
            state to emptySet()
        }

    private fun handleWidgetClicked(
        state: State
    ): InterviewPreparationWidgetReducerResult =
        if (state is State.Content && state.steps.isNotEmpty()) {
            state to setOf(
                InternalAction.FetchOnboardingFlag,
                InternalAction.LogAnalyticEvent(
                    InterviewPreparationWidgetClickedHyperskillAnalyticsEvent
                )
            )
        } else {
            state to emptySet()
        }

    private fun handleStepSolved(
        state: State,
        message: InternalMessage.StepSolved
    ): InterviewPreparationWidgetReducerResult =
        if (state is State.Content) {
            state.copy(
                steps = state.steps.mutate { remove(message.stepId) }
            ) to emptySet()
        } else {
            state to emptySet()
        }

    private fun handleOnboardingFlagFetchResult(
        state: State,
        message: InternalMessage.OnboardingFlagFetchResult
    ): InterviewPreparationWidgetReducerResult {
        if (state !is State.Content || state.steps.isEmpty()) return state to emptySet()
        val stepId = state.steps.shuffled().first()
        val stepRoute = StepRoute.InterviewPreparation(stepId.id)
        val navigationAction = when (message) {
            is InternalMessage.OnboardingFlagFetchResult.Success ->
                if (!message.wasOnboardingShown) {
                    Action.ViewAction.NavigateTo.InterviewPreparationOnboarding(stepRoute)
                } else {
                    Action.ViewAction.NavigateTo.Step(stepRoute)
                }
            InternalMessage.OnboardingFlagFetchResult.Error ->
                Action.ViewAction.NavigateTo.Step(stepRoute)
        }
        return state to setOf(navigationAction)
    }
}