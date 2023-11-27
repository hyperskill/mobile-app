package org.hyperskill.app.leaderboards.screen.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent

object LeaderboardsScreenFeature {
    internal sealed interface State {
        object Idle : State
        object Loading : State
        object Error : State
        data class Content(
            val isRefreshing: Boolean = false
        ) : State
    }

    sealed interface Message {
        object Initialize : Message
        object RetryContentLoading : Message

        object PullToRefresh : Message

        object ViewedEventMessage : Message
    }

    internal sealed interface InternalMessage : Message {
        object FetchLeaderboardsError : InternalMessage
        object FetchLeaderboardsSuccess : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action
    }

    internal sealed interface InternalAction : Action {
        object FetchLeaderboards : InternalAction

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}