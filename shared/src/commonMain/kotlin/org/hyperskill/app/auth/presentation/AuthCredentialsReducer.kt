package org.hyperskill.app.auth.presentation

import org.hyperskill.app.auth.presentation.AuthCredentialsFeature.Action
import org.hyperskill.app.auth.presentation.AuthCredentialsFeature.Message
import org.hyperskill.app.auth.presentation.AuthCredentialsFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class AuthCredentialsReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.AuthEditing -> {
                if (state.formState is AuthCredentialsFeature.FormState.Editing || state.formState is AuthCredentialsFeature.FormState.Error) {
                    State(message.email, message.password, AuthCredentialsFeature.FormState.Editing) to emptySet()
                } else {
                    null
                }
            }
            is Message.AuthWithEmail -> {
                if (state.formState is AuthCredentialsFeature.FormState.Editing || state.formState is AuthCredentialsFeature.FormState.Error) {
                    state.copy(formState = AuthCredentialsFeature.FormState.Loading) to setOf(Action.AuthWithEmail(state.email, state.password))
                } else {
                    null
                }
            }
            is Message.AuthSuccess -> {
                if (state.formState is AuthCredentialsFeature.FormState.Loading) {
                    state.copy(formState = AuthCredentialsFeature.FormState.Authenticated) to setOf(Action.ViewAction.NavigateToHomeScreen)
                } else {
                    null
                }
            }
            is Message.AuthFailure -> {
                if (state.formState is AuthCredentialsFeature.FormState.Loading) {
                    // TODO Handle error
                    state.copy(formState = AuthCredentialsFeature.FormState.Error("Error")) to emptySet()
                } else {
                    null
                }
            }
        } ?: state to emptySet()
}