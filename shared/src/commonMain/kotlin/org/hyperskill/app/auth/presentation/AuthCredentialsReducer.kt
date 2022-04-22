package org.hyperskill.app.auth.presentation

import org.hyperskill.app.auth.presentation.AuthCredentialsFeature.Action
import org.hyperskill.app.auth.presentation.AuthCredentialsFeature.Message
import org.hyperskill.app.auth.presentation.AuthCredentialsFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class AuthCredentialsReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.AuthWithEmail -> {
                if (state is State.Idle || state is State.Error) {
                    State.Loading to setOf(Action.AuthWithEmail(message.email, message.password))
                } else {
                    null
                }
            }
            is Message.AuthSuccess -> {
                if (state is State.Loading) {
                    State.Authenticated to setOf(Action.ViewAction.NavigateToHomeScreen)
                } else {
                    null
                }
            }
            is Message.AuthFailure -> {
                if (state is State.Loading) {
                    State.Error to setOf(Action.ViewAction.ShowAuthError)
                } else {
                    null
                }
            }
        } ?: state to emptySet()
}