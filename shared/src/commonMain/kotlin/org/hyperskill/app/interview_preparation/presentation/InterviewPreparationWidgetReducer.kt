package org.hyperskill.app.interview_preparation.presentation

import org.hyperskill.app.interview_preparation.domain.analytic.InterviewPreparationWidgetClickedHyperskillAnalyticsEvent
import org.hyperskill.app.interview_preparation.domain.analytic.InterviewPreparationWidgetClickedRetryContentLoadingHyperskillAnalyticsEvent
import org.hyperskill.app.interview_preparation.domain.analytic.InterviewPreparationWidgetViewedHyperskillAnalyticsEvent
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
            is InternalMessage.Initialize -> handleInitializeMessage(state, message)
            is InternalMessage.FetchInterviewStepsResultSuccess -> {
                State.Content(stepsCount = message.steps.count()) to emptySet()
            }
            is InternalMessage.FetchInterviewStepsResultError -> {
                State.Error to emptySet()
            }
            InternalMessage.PullToRefresh -> handlePullToRefresh(state)
            Message.RetryContentLoading -> handleRetryContentLoading(state)
            Message.ViewedEventMessage -> handleWidgetViewed(state)
            Message.WidgetClicked -> handleWidgetClicked(state)
            is InternalMessage.StepsCountChanged -> handleStepCountChanged(state, message)
            is InternalMessage.FetchNextInterviewStepResultSuccess ->
                handleFetchNextInterviewStepResultSuccess(state, message)
            is InternalMessage.FetchNextInterviewStepResultError -> {
                state to setOf(Action.ViewAction.ShowOpenStepError(message.errorMessage))
            }
            is InternalMessage.OnboardingFlagFetchResultSuccess ->
                handleOnboardingFlagFetchSuccessResult(state, message)
            InternalMessage.OnboardingFlagFetchResultError ->
                handleOnboardingFlagFetchErrorResult(state)
        }

    private fun handleInitializeMessage(
        state: State,
        message: InternalMessage.Initialize
    ): InterviewPreparationWidgetReducerResult =
        when (state) {
            State.Idle ->
                State.Loading(isLoadingSilently = false) to
                    setOf(InternalAction.FetchInterviewSteps(false))
            State.Error ->
                if (message.forceUpdate) {
                    State.Loading(isLoadingSilently = false) to
                        setOf(InternalAction.FetchInterviewSteps(false))
                } else {
                    state to emptySet()
                }
            is State.Content ->
                if (message.forceUpdate) {
                    State.Loading(
                        isLoadingSilently = state.stepsCount == 0
                    ) to setOf(InternalAction.FetchInterviewSteps(true))
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
                    state.copy(isRefreshing = true) to
                        setOf(InternalAction.FetchInterviewSteps(true))
                }
            is State.Error ->
                State.Loading(isLoadingSilently = false) to
                    setOf(InternalAction.FetchInterviewSteps(true))
            else ->
                state to emptySet()
        }

    private fun handleRetryContentLoading(
        state: State
    ): InterviewPreparationWidgetReducerResult =
        if (state is State.Error) {
            State.Loading(isLoadingSilently = false) to setOf(
                InternalAction.FetchInterviewSteps(true),
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
        if (state is State.Content) {
            state to setOf(
                InternalAction.LogAnalyticEvent(
                    InterviewPreparationWidgetClickedHyperskillAnalyticsEvent
                ),
                InternalAction.FetchOnboardingFlag
            )
        } else {
            state to emptySet()
        }

    private fun handleFetchNextInterviewStepResultSuccess(
        state: State,
        message: InternalMessage.FetchNextInterviewStepResultSuccess
    ) =
        state to setOf(
            Action.ViewAction.NavigateTo.Step(
                StepRoute.InterviewPreparation(message.stepId)
            )
        )

    private fun handleWidgetViewed(
        state: State
    ): InterviewPreparationWidgetReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(
                InterviewPreparationWidgetViewedHyperskillAnalyticsEvent
            )
        )

    private fun handleStepCountChanged(
        state: State,
        message: InternalMessage.StepsCountChanged
    ): InterviewPreparationWidgetReducerResult =
        if (state is State.Content) {
            state.copy(stepsCount = message.stepsCount) to emptySet()
        } else {
            state to emptySet()
        }

    private fun handleOnboardingFlagFetchSuccessResult(
        state: State,
        message: InternalMessage.OnboardingFlagFetchResultSuccess
    ): InterviewPreparationWidgetReducerResult {
        if (state !is State.Content) return state to emptySet()
        val stepId = state.steps.shuffled().first()
        val stepRoute = StepRoute.InterviewPreparation(stepId.id)
        val navigationAction = if (!message.wasOnboardingShown) {
            Action.ViewAction.NavigateTo.InterviewPreparationOnboarding(stepRoute)
        } else {
            Action.ViewAction.NavigateTo.Step(stepRoute)
        }
        return state to setOf(navigationAction)
    }

    private fun handleOnboardingFlagFetchErrorResult(
        state: State
    ): InterviewPreparationWidgetReducerResult {
        if (state !is State.Content || state.steps.isEmpty()) return state to emptySet()
        val stepId = state.steps.shuffled().first()
        return state to setOf(
            Action.ViewAction.NavigateTo.Step(
                StepRoute.InterviewPreparation(stepId.id)
            )
        )
    }
}