package org.hyperskill.app.auth.presentation

import org.hyperskill.app.auth.presentation.AuthSocialFeature.Action
import org.hyperskill.app.auth.presentation.AuthSocialFeature.Message
import org.hyperskill.app.auth.presentation.AuthSocialFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class AuthSocialReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.AuthWithSocial -> {
                if (state is State.Idle || state is State.Error) {
                    State.Loading to setOf(Action.AuthWithSocial(message.authCode, message.socialAuthProvider))
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