package org.hyperskill.app.auth.presentation

import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature.Action
import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature.Message
import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class AuthSocialWebViewReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.InitMessage -> {
                if (state is State.Idle) {
                    State.Loading(message.url) to emptySet()
                } else {
                    null
                }
            }
            is Message.PageLoaded -> {
                if (state is State.Loading) {
                    State.Content to emptySet()
                } else {
                    null
                }
            }
            is Message.AuthCodeSuccess -> {
                if (state is State.Content) {
                    state to setOf(Action.CallbackAuthCode(message.authCode, message.socialAuthProvider))
                } else {
                    null
                }
            }
            is Message.AuthCodeFailure -> {
                state to setOf(Action.CallbackAuthError(message.socialError, message.originalError))
            }
        } ?: (state to emptySet())
}