package org.hyperskill.app.leaderboards.widget.presentation

import org.hyperskill.app.leaderboards.widget.domain.analytic.LeaderboardWidgetClickedListItemHyperskillAnalyticEvent
import org.hyperskill.app.leaderboards.widget.presentation.LeaderboardWidgetFeature.Action
import org.hyperskill.app.leaderboards.widget.presentation.LeaderboardWidgetFeature.InternalAction
import org.hyperskill.app.leaderboards.widget.presentation.LeaderboardWidgetFeature.Message
import org.hyperskill.app.leaderboards.widget.presentation.LeaderboardWidgetFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias LeaderboardWidgetReducerResult = Pair<State, Set<Action>>

class LeaderboardWidgetReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): LeaderboardWidgetReducerResult =
        when (message) {
            is LeaderboardWidgetFeature.InternalMessage.Initialize -> {
                if (state is State.Idle ||
                    (message.forceUpdate && (state is State.Content || state is State.Error))
                ) {
                    State.Loading to setOf(InternalAction.FetchLeaderboardData)
                } else {
                    null
                }
            }
            LeaderboardWidgetFeature.InternalMessage.PullToRefresh -> {
                when (state) {
                    is State.Content -> {
                        if (state.isRefreshing) {
                            null
                        } else {
                            state.copy(isRefreshing = true) to setOf(InternalAction.FetchLeaderboardData)
                        }
                    }
                    State.Error ->
                        State.Loading to setOf(InternalAction.FetchLeaderboardData)
                    State.Idle, State.Loading -> null
                }
            }
            LeaderboardWidgetFeature.InternalMessage.ReloadContentInBackground -> {
                when (state) {
                    is State.Content -> {
                        if (state.isRefreshing) {
                            null
                        } else {
                            state to setOf(InternalAction.FetchLeaderboardData)
                        }
                    }
                    State.Error ->
                        State.Loading to setOf(InternalAction.FetchLeaderboardData)
                    State.Idle, State.Loading -> null
                }
            }
            is LeaderboardWidgetFeature.InternalMessage.LeaderboardItemClickedEventMessage -> {
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        LeaderboardWidgetClickedListItemHyperskillAnalyticEvent(
                            currentTab = message.currentTab,
                            leaderboardItem = message.leaderboardItem
                        )
                    )
                )
            }
        } ?: (state to emptySet())
}