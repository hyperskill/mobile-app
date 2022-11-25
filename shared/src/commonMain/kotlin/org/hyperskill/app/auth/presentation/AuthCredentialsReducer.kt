package org.hyperskill.app.auth.presentation

import org.hyperskill.app.auth.domain.analytic.AuthCredentialsClickedContinueWithSocialHyperskillAnalyticEvent
import org.hyperskill.app.auth.domain.analytic.AuthCredentialsClickedResetPasswordHyperskillAnalyticEvent
import org.hyperskill.app.auth.domain.analytic.AuthCredentialsClickedSignInHyperskillAnalyticEvent
import org.hyperskill.app.auth.domain.analytic.AuthCredentialsViewedHyperskillAnalyticEvent
import org.hyperskill.app.auth.domain.model.AuthCredentialsError
import org.hyperskill.app.auth.presentation.AuthCredentialsFeature.Action
import org.hyperskill.app.auth.presentation.AuthCredentialsFeature.Message
import org.hyperskill.app.auth.presentation.AuthCredentialsFeature.State
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
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
            is Message.SubmitFormClicked -> {
                if (state.formState is AuthCredentialsFeature.FormState.Editing || state.formState is AuthCredentialsFeature.FormState.Error) {
                    when {
                        state.email.isBlank() ->
                            state.copy(formState = AuthCredentialsFeature.FormState.Error(AuthCredentialsError.ERROR_EMAIL_EMPTY)) to emptySet()
                        state.password.isBlank() ->
                            state.copy(formState = AuthCredentialsFeature.FormState.Error(AuthCredentialsError.ERROR_PASSWORD_EMPTY)) to emptySet()
                        else ->
                            state.copy(formState = AuthCredentialsFeature.FormState.Loading) to setOf(Action.AuthWithEmail(state.email, state.password))
                    }
                } else {
                    null
                }
            }
            is Message.AuthSuccess -> {
                if (state.formState is AuthCredentialsFeature.FormState.Loading) {
                    state.copy(formState = AuthCredentialsFeature.FormState.Authenticated) to setOf(Action.ViewAction.CompleteAuthFlow(message.isNewUser))
                } else {
                    null
                }
            }
            is Message.AuthFailure -> {
                if (state.formState is AuthCredentialsFeature.FormState.Loading) {
                    state.copy(formState = AuthCredentialsFeature.FormState.Error(message.credentialsError)) to
                        setOf(Action.ViewAction.CaptureError(message.originalError))
                } else {
                    null
                }
            }
            is Message.ClickedResetPassword -> {
                state.copy(isLoadingMagicLink = true) to setOf(
                    Action.GetMagicLink(HyperskillUrlPath.ResetPassword()),
                    Action.LogAnalyticEvent(AuthCredentialsClickedResetPasswordHyperskillAnalyticEvent())
                )
            }
            is Message.GetMagicLinkReceiveSuccess -> {
                state.copy(isLoadingMagicLink = false) to setOf(Action.ViewAction.OpenUrl(message.url))
            }
            is Message.GetMagicLinkReceiveFailure -> {
                state.copy(isLoadingMagicLink = false) to setOf(Action.ViewAction.ShowGetMagicLinkError)
            }
            is Message.ViewedEventMessage ->
                state to setOf(Action.LogAnalyticEvent(AuthCredentialsViewedHyperskillAnalyticEvent()))
            is Message.ClickedSignInEventMessage ->
                state to setOf(Action.LogAnalyticEvent(AuthCredentialsClickedSignInHyperskillAnalyticEvent()))
            is Message.ClickedContinueWithSocialEventMessage ->
                state to setOf(Action.LogAnalyticEvent(AuthCredentialsClickedContinueWithSocialHyperskillAnalyticEvent()))
        } ?: (state to emptySet())
}