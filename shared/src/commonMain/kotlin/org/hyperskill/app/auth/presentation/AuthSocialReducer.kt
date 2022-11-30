package org.hyperskill.app.auth.presentation

import org.hyperskill.app.auth.domain.analytic.AuthSocialClickedContinueWithEmailHyperskillAnalyticEvent
import org.hyperskill.app.auth.domain.analytic.AuthSocialClickedSignInWithSocialHyperskillAnalyticEvent
import org.hyperskill.app.auth.domain.analytic.AuthSocialViewedHyperskillAnalyticEvent
import org.hyperskill.app.auth.domain.model.AuthSocialError
import org.hyperskill.app.auth.presentation.AuthSocialFeature.Action
import org.hyperskill.app.auth.presentation.AuthSocialFeature.Message
import org.hyperskill.app.auth.presentation.AuthSocialFeature.State
import org.hyperskill.app.sentry.domain.model.breadcrumb.HyperskillSentryBreadcrumbBuilder
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
                    State.Authenticated to setOf(
                        Action.AddSentryBreadcrumb(
                            HyperskillSentryBreadcrumbBuilder.buildAuthSocialSignedInSuccessfully(
                                message.socialAuthProvider
                            )
                        ),
                        Action.ViewAction.CompleteAuthFlow(message.profile)
                    )
                } else {
                    null
                }
            }
            is Message.AuthFailure -> {
                if (state is State.Loading) {
                    State.Error to getAuthFailureActionsSet(message.data)
                } else {
                    null
                }
            }
            is Message.SocialAuthProviderAuthFailureEventMessage ->
                state to getAuthFailureActionsSet(message.data)
            is Message.ViewedEventMessage ->
                state to setOf(Action.LogAnalyticEvent(AuthSocialViewedHyperskillAnalyticEvent()))
            is Message.ClickedSignInWithSocialEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(AuthSocialClickedSignInWithSocialHyperskillAnalyticEvent(message.socialAuthProvider)),
                    Action.AddSentryBreadcrumb(HyperskillSentryBreadcrumbBuilder.buildAuthSocialSigningIn(message.socialAuthProvider))
                )
            is Message.ClickedContinueWithEmailEventMessage ->
                state to setOf(Action.LogAnalyticEvent(AuthSocialClickedContinueWithEmailHyperskillAnalyticEvent()))
        } ?: (state to emptySet())

    private fun getAuthFailureActionsSet(data: Message.AuthFailureData): Set<Action> =
        setOf(
            Action.ViewAction.ShowAuthError(
                socialAuthError = data.socialAuthError ?: AuthSocialError.CONNECTION_PROBLEM,
                originalError = data.originalError
            ),
            Action.AddSentryBreadcrumb(HyperskillSentryBreadcrumbBuilder.buildAuthSocialSignInFailed(data.socialAuthProvider)),
            Action.CaptureSentryAuthError(data.socialAuthError, data.originalError)
        )
}