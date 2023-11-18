package org.hyperskill.app.challenges.widget.presentation

import org.hyperskill.app.challenges.domain.model.ChallengeStatus
import org.hyperskill.app.challenges.domain.model.ChallengeTargetType
import org.hyperskill.app.challenges.widget.domain.analytic.ChallengeWidgetClickedCollectRewardHyperskillAnalyticEvent
import org.hyperskill.app.challenges.widget.domain.analytic.ChallengeWidgetClickedDeadlineReloadHyperskillAnalyticEvent
import org.hyperskill.app.challenges.widget.domain.analytic.ChallengeWidgetClickedLinkInTheDescriptionHyperskillAnalyticEvent
import org.hyperskill.app.challenges.widget.domain.analytic.ChallengeWidgetClickedRetryContentLoadingHyperskillAnalyticEvent
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature.Action
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature.InternalAction
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature.InternalMessage
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature.Message
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ChallengeWidgetReducerResult = Pair<State, Set<Action>>

class ChallengeWidgetReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ChallengeWidgetReducerResult =
        when (message) {
            is InternalMessage.Initialize ->
                handleInitializeMessage(state, message)
            InternalMessage.FetchChallengesError ->
                State.Error to emptySet()
            is InternalMessage.FetchChallengesSuccess ->
                handleFetchChallengesSuccessMessage(message)
            Message.RetryContentLoading ->
                handleRetryContentLoadingMessage(state)
            InternalMessage.PullToRefresh ->
                handlePullToRefreshMessage(state)
            is Message.LinkInTheDescriptionClicked ->
                handleLinkInTheDescriptionClickedMessage(state, message)
            Message.DeadlineReachedReloadClicked ->
                handleDeadlineReachedReloadClickedMessage(state)
            Message.CollectRewardClicked ->
                handleCollectRewardClickedMessage(state)
            InternalMessage.CreateMagicLinkError ->
                handleCreateMagicLinkFailureMessage(state)
            is InternalMessage.CreateMagicLinkSuccess ->
                handleCreateMagicLinkSuccessMessage(state, message)
            InternalMessage.StepSolved ->
                handleStepSolvedMessage(state)
            InternalMessage.DailyStepCompleted ->
                handleDailyStepCompletedMessage(state)
            InternalMessage.TopicCompleted ->
                handleTopicCompletedMessage(state)
            InternalMessage.TimerTick ->
                handleTimerTickMessage(state)
        } ?: (state to emptySet())

    private fun handleInitializeMessage(
        state: State,
        message: InternalMessage.Initialize
    ): ChallengeWidgetReducerResult? =
        when (state) {
            State.Idle ->
                State.Loading(isLoadingSilently = false) to setOf(InternalAction.FetchChallenges)
            State.Error ->
                if (message.forceUpdate) {
                    State.Loading(isLoadingSilently = false) to setOf(InternalAction.FetchChallenges)
                } else {
                    null
                }
            is State.Content ->
                if (message.forceUpdate) {
                    State.Loading(
                        isLoadingSilently = state.challenges.isEmpty()
                    ) to setOf(InternalAction.FetchChallenges)
                } else {
                    null
                }
            is State.Loading -> null
        }

    private fun handleFetchChallengesSuccessMessage(
        message: InternalMessage.FetchChallengesSuccess
    ): ChallengeWidgetReducerResult {
        val newState = State.Content(challenges = message.challenges)

        val actions = when (newState.getCurrentChallenge()?.status) {
            ChallengeStatus.NOT_STARTED,
            ChallengeStatus.STARTED ->
                setOf(InternalAction.LaunchTimer)
            else ->
                emptySet()
        }

        return newState to actions
    }

    private fun handleRetryContentLoadingMessage(state: State): ChallengeWidgetReducerResult? =
        if (state is State.Error) {
            State.Loading(isLoadingSilently = false) to setOf(
                InternalAction.FetchChallenges,
                InternalAction.LogAnalyticEvent(ChallengeWidgetClickedRetryContentLoadingHyperskillAnalyticEvent)
            )
        } else {
            null
        }

    private fun handlePullToRefreshMessage(state: State): ChallengeWidgetReducerResult? =
        when (state) {
            is State.Content ->
                if (state.isRefreshing) {
                    null
                } else {
                    state.copy(isRefreshing = true) to setOf(InternalAction.FetchChallenges)
                }
            State.Error ->
                State.Loading(isLoadingSilently = false) to setOf(InternalAction.FetchChallenges)
            else ->
                null
        }

    private fun handleLinkInTheDescriptionClickedMessage(
        state: State,
        message: Message.LinkInTheDescriptionClicked
    ): ChallengeWidgetReducerResult? =
        if (state is State.Content) {
            state to setOf(
                Action.ViewAction.OpenUrl(url = message.url, shouldOpenInApp = true),
                InternalAction.LogAnalyticEvent(
                    ChallengeWidgetClickedLinkInTheDescriptionHyperskillAnalyticEvent(
                        challengeId = state.getCurrentChallenge()?.id,
                        url = message.url
                    )
                )
            )
        } else {
            null
        }

    private fun handleDeadlineReachedReloadClickedMessage(state: State): ChallengeWidgetReducerResult? =
        if (state is State.Content) {
            val newState = if (state.isRefreshing) {
                state
            } else {
                State.Loading(isLoadingSilently = false)
            }

            newState to setOf(
                InternalAction.LogAnalyticEvent(
                    ChallengeWidgetClickedDeadlineReloadHyperskillAnalyticEvent(
                        challengeId = state.getCurrentChallenge()?.id
                    )
                )
            )
        } else {
            null
        }

    private fun handleCollectRewardClickedMessage(state: State): ChallengeWidgetReducerResult =
        state to buildSet {
            state.getCurrentChallenge()?.rewardLink?.let {
                add(InternalAction.CreateMagicLink(nextUrl = it))
            }
            add(
                InternalAction.LogAnalyticEvent(
                    ChallengeWidgetClickedCollectRewardHyperskillAnalyticEvent(
                        challengeId = state.getCurrentChallenge()?.id
                    )
                )
            )
        }

    private fun handleCreateMagicLinkFailureMessage(state: State): ChallengeWidgetReducerResult? =
        if (state is State.Content) {
            state.copy(isLoadingMagicLink = false) to setOf(Action.ViewAction.ShowNetworkError)
        } else {
            null
        }

    private fun handleCreateMagicLinkSuccessMessage(
        state: State,
        message: InternalMessage.CreateMagicLinkSuccess
    ): ChallengeWidgetReducerResult? =
        if (state is State.Content) {
            state.copy(isLoadingMagicLink = false) to setOf(
                Action.ViewAction.OpenUrl(url = message.url, shouldOpenInApp = false)
            )
        } else {
            null
        }

    private fun handleStepSolvedMessage(state: State): ChallengeWidgetReducerResult? {
        if (state is State.Content) {
            val currentChallenge = state.getCurrentChallenge() ?: return null
            val currentChallengeTargetType = currentChallenge.targetType

            if (currentChallengeTargetType != null) {
                return when (currentChallengeTargetType) {
                    ChallengeTargetType.DAILY_STEP,
                    ChallengeTargetType.TOPIC ->
                        null
                    ChallengeTargetType.PROJECT,
                    ChallengeTargetType.STAGE ->
                        state to setOf(InternalAction.FetchChallenges)
                    ChallengeTargetType.STEP ->
                        state.copy(
                            challenges = state.setCurrentChallengeIntervalProgressAsCompleted() ?: state.challenges
                        ) to emptySet()
                }
            } else {
                return state to setOf(InternalAction.FetchChallenges)
            }
        } else {
            return null
        }
    }

    private fun handleDailyStepCompletedMessage(state: State): ChallengeWidgetReducerResult? =
        if (state is State.Content &&
            state.getCurrentChallenge()?.targetType == ChallengeTargetType.DAILY_STEP
        ) {
            state.copy(
                challenges = state.setCurrentChallengeIntervalProgressAsCompleted() ?: state.challenges
            ) to emptySet()
        } else {
            null
        }

    private fun handleTopicCompletedMessage(state: State): ChallengeWidgetReducerResult? =
        if (state is State.Content &&
            state.getCurrentChallenge()?.targetType == ChallengeTargetType.TOPIC
        ) {
            state.copy(
                challenges = state.setCurrentChallengeIntervalProgressAsCompleted() ?: state.challenges
            ) to emptySet()
        } else {
            null
        }

    private fun handleTimerTickMessage(state: State): ChallengeWidgetReducerResult =
        when (state) {
            State.Idle,
            State.Error,
            is State.Loading ->
                state to setOf(InternalAction.StopTimer)
            is State.Content -> {
                when (state.getCurrentChallenge()?.status) {
                    null,
                    ChallengeStatus.COMPLETED,
                    ChallengeStatus.PARTIAL_COMPLETED,
                    ChallengeStatus.NOT_COMPLETED ->
                        state to setOf(InternalAction.StopTimer)
                    ChallengeStatus.NOT_STARTED,
                    ChallengeStatus.STARTED ->
                        state to emptySet()
                }
            }
        }
}