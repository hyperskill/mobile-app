package org.hyperskill.app.challenges.widget.presentation

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
                State.Content(challenges = message.challenges) to emptySet()
            Message.RetryContentLoading ->
                handleRetryContentLoadingMessage(state)
            InternalMessage.PullToRefresh ->
                handlePullToRefreshMessage(state)
        } ?: (state to emptySet())

    private fun handleInitializeMessage(
        state: State,
        message: InternalMessage.Initialize
    ): ChallengeWidgetReducerResult? =
        if (state is State.Idle ||
            (message.forceUpdate && (state is State.Content || state is State.Error))
        ) {
            State.Loading to setOf(InternalAction.FetchChallenges)
        } else {
            null
        }

    private fun handleRetryContentLoadingMessage(state: State): ChallengeWidgetReducerResult? =
        if (state is State.Error) {
            State.Loading to setOf(
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
                State.Loading to setOf(InternalAction.FetchChallenges)
            else ->
                null
        }
}