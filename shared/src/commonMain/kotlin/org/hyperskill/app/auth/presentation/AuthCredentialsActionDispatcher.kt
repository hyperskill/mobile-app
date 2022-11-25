package org.hyperskill.app.auth.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.auth.domain.exception.AuthCredentialsException
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.auth.domain.model.AuthCredentialsError
import org.hyperskill.app.auth.presentation.AuthCredentialsFeature.Action
import org.hyperskill.app.auth.presentation.AuthCredentialsFeature.Message
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class AuthCredentialsActionDispatcher(
    config: ActionDispatcherOptions,
    private val authInteractor: AuthInteractor,
    private val profileInteractor: ProfileInteractor,
    private val urlPathProcessor: UrlPathProcessor,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.AuthWithEmail -> {
                val result = authInteractor.authWithEmail(action.email, action.password)

                val message =
                    result
                        .fold(
                            onSuccess = {
                                profileInteractor
                                    .getCurrentProfile(DataSourceType.REMOTE)
                                    .fold(
                                        onSuccess = { Message.AuthSuccess(isNewUser = it.trackId == null) },
                                        onFailure = { Message.AuthFailure(AuthCredentialsError.CONNECTION_PROBLEM, it) }
                                    )
                            },
                            onFailure = {
                                val error =
                                    if (it is AuthCredentialsException) {
                                        it.authCredentialsError
                                    } else {
                                        AuthCredentialsError.CONNECTION_PROBLEM
                                    }
                                Message.AuthFailure(error, it)
                            }
                        )
                onNewMessage(message)
            }
            is Action.GetMagicLink ->
                getLink(action.path, ::onNewMessage)
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            is Action.AddSentryBreadcrumb ->
                sentryInteractor.addBreadcrumb(action.breadcrumb)
            is Action.CaptureSentryException ->
                sentryInteractor.captureErrorMessage("AuthCredentials: ${action.throwable}")
        }
    }

    private suspend fun getLink(
        path: HyperskillUrlPath,
        onNewMessage: (Message) -> Unit
    ) {
        urlPathProcessor.processUrlPath(path)
            .fold(
                onSuccess = { url ->
                    onNewMessage(Message.GetMagicLinkReceiveSuccess(url))
                },
                onFailure = {
                    onNewMessage(Message.GetMagicLinkReceiveFailure)
                }
            )
    }
}