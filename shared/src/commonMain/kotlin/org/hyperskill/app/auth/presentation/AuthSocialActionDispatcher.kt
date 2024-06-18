package org.hyperskill.app.auth.presentation

import org.hyperskill.app.auth.domain.exception.AuthSocialException
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.auth.domain.model.AuthSocialError
import org.hyperskill.app.auth.presentation.AuthSocialFeature.Action
import org.hyperskill.app.auth.presentation.AuthSocialFeature.Message
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class AuthSocialActionDispatcher(
    config: ActionDispatcherOptions,
    private val authInteractor: AuthInteractor,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private var sentryInteractor: SentryInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.AuthWithSocial -> {
                val sentryTransaction = HyperskillSentryTransactionBuilder.buildAuthSocialAuth()
                sentryInteractor.startTransaction(sentryTransaction)

                val result = authInteractor.authWithSocial(action.authCode, action.idToken, action.socialAuthProvider)

                val message =
                    result
                        .fold(
                            onSuccess = {
                                currentProfileStateRepository
                                    .getState(forceUpdate = true)
                                    .fold(
                                        onSuccess = {
                                            Message.AuthSuccess(
                                                socialAuthProvider = action.socialAuthProvider,
                                                profile = it
                                            )
                                        },
                                        onFailure = {
                                            Message.AuthFailure(
                                                Message.AuthFailureData(
                                                    socialAuthProvider = action.socialAuthProvider,
                                                    socialAuthError = AuthSocialError.ConnectionProblem,
                                                    originalError = it
                                                )
                                            )
                                        }
                                    )
                            },
                            onFailure = {
                                val error =
                                    if (it is AuthSocialException) {
                                        it.authSocialError
                                    } else {
                                        AuthSocialError.ConnectionProblem
                                    }
                                Message.AuthFailure(
                                    Message.AuthFailureData(
                                        socialAuthProvider = action.socialAuthProvider,
                                        socialAuthError = error,
                                        originalError = it
                                    )
                                )
                            }
                        )

                sentryInteractor.finishTransaction(
                    transaction = sentryTransaction,
                    throwable = (message as? Message.AuthFailure)?.data?.originalError
                )

                onNewMessage(message)
            }
            is Action.AddSentryBreadcrumb ->
                sentryInteractor.addBreadcrumb(action.breadcrumb)
            is Action.CaptureSentryAuthError -> {
                if (action.socialAuthError != null) {
                    sentryInteractor.captureErrorMessage(
                        "AuthSocial: ${action.socialAuthError}, ${action.originalError}"
                    )
                } else {
                    sentryInteractor.captureErrorMessage("AuthSocial: ${action.originalError}")
                }
            }
            else -> {}
        }
    }
}