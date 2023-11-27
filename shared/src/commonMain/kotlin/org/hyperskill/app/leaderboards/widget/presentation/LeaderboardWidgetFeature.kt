package org.hyperskill.app.leaderboards.widget.presentation

object LeaderboardWidgetFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        object Error : State
        data class Content(
            val isRefreshing: Boolean = false
        ) : State
    }

    internal val State.isRefreshing: Boolean
        get() = this is State.Content && isRefreshing

    sealed interface ViewState {
        object Idle : ViewState
        object Loading : ViewState
        object Error : ViewState
        object Empty : ViewState
        object Content : ViewState
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