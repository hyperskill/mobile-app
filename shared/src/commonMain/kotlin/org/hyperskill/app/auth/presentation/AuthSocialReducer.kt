package org.hyperskill.app.auth.presentation

import org.hyperskill.app.auth.domain.analytic.AuthSocialClickedContinueWithEmailHyperskillAnalyticEvent
import org.hyperskill.app.auth.domain.analytic.AuthSocialClickedSignInWithSocialHyperskillAnalyticEvent
import org.hyperskill.app.auth.domain.analytic.AuthSocialViewedHyperskillAnalyticEvent
import org.hyperskill.app.auth.presentation.AuthSocialFeature.Action
import org.hyperskill.app.auth.presentation.AuthSocialFeature.Message
import org.hyperskill.app.auth.presentation.AuthSocialFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class AuthSocialReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.AuthWithSocial -> {
                if (state is State.Idle || state is State.Error) {
                    State.Loading to setOf(
                        Action.AuthWithSocial(
                            message.authCode,
                            message.idToken,
                            message.socialAuthProvider
                        )
                    )
                } else {
                    null
                }
            }
            is Message.AuthSuccess -> {
                if (state is State.Loading) {
                    State.Authenticated to setOf(Action.ViewAction.CompleteAuthFlow(message.isNewUser))
                } else {
                    null
                }
            }
            is Message.AuthFailure -> {
                if (state is State.Loading) {
                    State.Error to setOf(Action.ViewAction.ShowAuthError(message.socialError))
                } else {
                    null
                }
            }
            is Message.AuthViewedEventMessage ->
                state to setOf(Action.LogAnalyticEvent(AuthSocialViewedHyperskillAnalyticEvent()))
            is Message.AuthClickedSignInWithSocialEventMessage ->
                state to setOf(Action.LogAnalyticEvent(AuthSocialClickedSignInWithSocialHyperskillAnalyticEvent(message.socialAuthProvider)))
            is Message.AuthClickedContinueWithEmailEventMessage ->
                state to setOf(Action.LogAnalyticEvent(AuthSocialClickedContinueWithEmailHyperskillAnalyticEvent()))
        } ?: (state to emptySet())
}