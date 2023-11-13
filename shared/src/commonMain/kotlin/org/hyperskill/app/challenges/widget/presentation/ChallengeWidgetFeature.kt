package org.hyperskill.app.challenges.widget.presentation

import org.hyperskill.app.challenges.domain.model.Challenge

object ChallengeWidgetFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        object Error : State
        data class Content(
            val challenges: List<Challenge>,
            internal val isRefreshing: Boolean = false
        ) : State
    }

    internal val State.isRefreshing: Boolean
        get() = this is State.Content && isRefreshing

    sealed interface Message

    internal sealed interface InternalMessage : Message {
        data class Initialize(val forceUpdate: Boolean = false) : InternalMessage
        object FetchChallengesError : InternalMessage
        data class FetchChallengesSuccess(val challenges: List<Challenge>) : InternalMessage

        object PullToRefresh : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action
    }

    internal sealed interface InternalAction : Action {
        object FetchChallenges : InternalAction
    }
}