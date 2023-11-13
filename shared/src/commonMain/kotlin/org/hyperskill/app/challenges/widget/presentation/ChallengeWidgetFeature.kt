package org.hyperskill.app.challenges.widget.presentation

import org.hyperskill.app.challenges.domain.model.Challenge

object ChallengeWidgetFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        data class Content(
            val challenges: List<Challenge>
        ) : State

        object NetworkError : State
    }

    sealed interface Message

    internal sealed interface InternalMessage : Message

    sealed interface Action {
        sealed interface ViewAction : Action
    }

    internal sealed interface InternalAction : Action
}