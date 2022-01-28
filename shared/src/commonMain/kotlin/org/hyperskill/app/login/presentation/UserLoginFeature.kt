package org.hyperskill.app.login.presentation

import org.hyperskill.app.user_list.domain.model.User
import org.hyperskill.app.user_list.presentation.UsersListFeature
import org.hyperskill.app.user_list.remote.model.UsersQuery

interface UserLoginFeature {
    sealed interface State {
        object NotAuthorized : State
        object Authorized : State
    }

    sealed interface Message {
    }

    sealed interface Action {
    }
}