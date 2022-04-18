package org.hyperskill.app.auth.presentation

import org.hyperskill.app.auth.presentation.AuthFeature.Action
import org.hyperskill.app.auth.presentation.AuthFeature.Message
import org.hyperskill.app.auth.presentation.AuthFeature.State
import org.hyperskill.app.auth.view.mapper.SocialAuthProviderNameMapper
import ru.nobird.app.presentation.redux.reducer.StateReducer

class AuthReducer : StateReducer<State, Message, Action> {
    override fun reduce(
        state: State,
        message: Message
    ): Pair<State, Set<Action>> =
        when (message) {
            is Message.AuthWithSocialToken ->
                if (state is State.Idle || state is State.Error) {
                    State.Loading to setOf(
                        Action.AuthWithSocialToken(
                            message.authCode,
                            SocialAuthProviderNameMapper.getName(message.provider)
                        )
                    )
                } else {
                    null
                }
            is Message.AuthWithCode ->
                if (state is State.Idle || state is State.Error) {
                    State.Loading to setOf(Action.AuthWithCode(message.authCode))
                } else {
                    null
                }
            is Message.AuthSuccess ->
                State.Authenticated(message.accessToken) to setOf(Action.ViewAction.NavigateToHomeScreen)
            is Message.AuthError ->
                State.Error to setOf(Action.ViewAction.ShowAuthError(message.errorMsg))
        } ?: state to emptySet()
}