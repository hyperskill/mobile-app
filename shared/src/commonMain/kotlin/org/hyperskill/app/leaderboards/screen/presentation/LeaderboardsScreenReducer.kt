package org.hyperskill.app.leaderboards.screen.presentation

import org.hyperskill.app.leaderboards.screen.domain.analytic.LeaderboardClickedPullToRefreshHyperskillAnalyticEvent
import org.hyperskill.app.leaderboards.screen.domain.analytic.LeaderboardViewedHyperskillAnalyticEvent
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenFeature.Action
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenFeature.InternalAction
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenFeature.Message
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

internal class LeaderboardsScreenReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            Message.Initialize -> TODO()
            Message.PullToRefresh -> {
                // TODO: Handle pull to refresh
                state to setOf(InternalAction.LogAnalyticEvent(LeaderboardClickedPullToRefreshHyperskillAnalyticEvent))
            }
            Message.RetryContentLoading -> TODO()
            LeaderboardsScreenFeature.InternalMessage.FetchLeaderboardsError -> TODO()
            LeaderboardsScreenFeature.InternalMessage.FetchLeaderboardsSuccess -> TODO()
            Message.ViewedEventMessage -> {
                state to setOf(InternalAction.LogAnalyticEvent(LeaderboardViewedHyperskillAnalyticEvent))
            }
        }
}