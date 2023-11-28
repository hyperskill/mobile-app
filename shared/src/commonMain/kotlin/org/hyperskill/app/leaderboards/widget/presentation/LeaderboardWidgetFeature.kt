package org.hyperskill.app.leaderboards.widget.presentation

import org.hyperskill.app.leaderboards.domain.model.LeaderboardItem
import org.hyperskill.app.leaderboards.widget.view.model.LeaderboardWidgetListItem

object LeaderboardWidgetFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        object Error : State
        data class Content(
            val dailyLeaderboard: List<LeaderboardItem>,
            val weeklyLeaderboard: List<LeaderboardItem>,
            val currentUserId: Long,
            val isRefreshing: Boolean = false
        ) : State
    }

    internal val State.isRefreshing: Boolean
        get() = this is State.Content && isRefreshing

    sealed interface ViewState {
        object Idle : ViewState
        object Loading : ViewState
        object Error : ViewState
        data class Content(
            val dailyLeaderboard: List<LeaderboardWidgetListItem>,
            val weeklyLeaderboard: List<LeaderboardWidgetListItem>,
            val isRefreshing: Boolean = false
        ) : ViewState
    }

    sealed interface Message

    internal sealed interface InternalMessage : Message {
        data class Initialize(val forceUpdate: Boolean = false) : InternalMessage

        object ReloadContentInBackground : InternalMessage

        object PullToRefresh : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action
    }

    internal sealed interface InternalAction : Action {
        object FetchLeaderboardData : InternalAction
    }
}