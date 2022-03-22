package org.hyperskill.app.auth.presentation

interface AuthFeature {
    sealed interface State {
        // TODO: add data class with auth answer
        object Idle : State
        object Loading : State
        object NetworkError : State
    }

    sealed interface Message
    sealed interface Action {
        sealed interface ViewAction : Action {
            object ShowNetworkError : ViewAction
        }
    }
}