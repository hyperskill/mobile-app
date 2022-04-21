package org.hyperskill.app.main.presentation

interface AppFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        data class Ready(val isAuthorized: Boolean) : State
    }

    sealed interface Message {
        object AppStarted : Message
        object UserAuthorized : Message
        object UserDeauthorized : Message
        data class UserAccountStatus(val isAuthorized: Boolean) : Message
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