package org.hyperskill.app.leaderboard.widget.presentation

import org.hyperskill.app.leaderboard.widget.domain.analytic.LeaderboardWidgetClickedListItemHyperskillAnalyticEvent
import org.hyperskill.app.leaderboard.widget.presentation.LeaderboardWidgetFeature.Action
import org.hyperskill.app.leaderboard.widget.presentation.LeaderboardWidgetFeature.InternalAction
import org.hyperskill.app.leaderboard.widget.presentation.LeaderboardWidgetFeature.Message
import org.hyperskill.app.leaderboard.widget.presentation.LeaderboardWidgetFeature.State
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
            LeaderboardWidgetFeature.InternalMessage.FetchLeaderboardDataError -> {
                when (state) {
                    is State.Content -> {
                        if (state.isRefreshing) {
                            state.copy(isRefreshing = false) to emptySet()
                        } else {
                            null
                        }
                    }
                    State.Loading -> State.Error to emptySet()
                    else -> null
                }
            }
            is LeaderboardWidgetFeature.InternalMessage.FetchLeaderboardDataSuccess -> {
                State.Content(
                    dailyLeaderboard = message.dailyLeaderboard,
                    weeklyLeaderboard = message.weeklyLeaderboard,
                    currentUserId = message.currentUserId
                ) to emptySet()
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