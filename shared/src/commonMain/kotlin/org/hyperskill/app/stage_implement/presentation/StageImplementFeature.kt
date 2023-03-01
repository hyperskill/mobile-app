package org.hyperskill.app.stage_implement.presentation

object StageImplementFeature {
    internal sealed interface State {
        object Idle : State
    }

    sealed interface ViewState {
        object Idle : ViewState
    }

    sealed interface Message

    sealed interface Action {
        sealed interface ViewAction : Action
    }
}