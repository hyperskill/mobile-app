package org.hyperskill.app.auth.presentation

import ru.nobird.app.presentation.redux.reducer.StateReducer
import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature.Action
import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature.Message
import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature.State

class AuthSocialWebViewReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.AuthCodeSuccess -> {
                if (state is State.Loading || state is State.Error) {
                    State.Loading to setOf(Action.CallbackAuthCode(message.authCode, message.socialAuthProvider))
                } else {
                    null
                }
            }
            is Message.AuthCodeFailure -> {
                if (state is State.Loading) {
                    State.Error to setOf(Action.CallbackAuthError(message.socialError))
                } else {
                    null
                }
            }
        } ?: state to emptySet()
}
