package org.hyperskill.app.login.presentation

interface UserLoginFeature {
    sealed interface State {
        object NotAuthorized : State
        object Authorized : State
    }

    sealed interface Message

    sealed interface Action
}