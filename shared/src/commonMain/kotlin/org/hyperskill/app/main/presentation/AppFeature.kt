package org.hyperskill.app.main.presentation

interface AppFeature {
    sealed interface State {
        object Idle : State
    }

    sealed interface Message {
        object AppStarted : Message
        object UserAuthorized : Message
        object UserDeauthorized : Message
    }

    sealed interface Action {
        object DetermineUserAccountStatus : Action
        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                object HomeScreen : NavigateTo
                object AuthScreen : NavigateTo
            }
        }
    }
}