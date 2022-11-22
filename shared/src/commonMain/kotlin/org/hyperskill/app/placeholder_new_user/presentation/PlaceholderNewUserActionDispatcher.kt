package org.hyperskill.app.placeholder_new_user.presentation

import kotlinx.coroutines.flow.MutableSharedFlow
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature.Action
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class PlaceholderNewUserActionDispatcher(
    config: ActionDispatcherOptions,
    private val analyticInteractor: AnalyticInteractor,
    private val authInteractor: AuthInteractor,
    private val authorizationFlow: MutableSharedFlow<UserDeauthorized>,
    private val urlPathProcessor: UrlPathProcessor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.Logout -> {
                val isAuthorized = authInteractor.isAuthorized()
                    .getOrDefault(false)

                if (isAuthorized) {
                    authorizationFlow.tryEmit(UserDeauthorized(reason = UserDeauthorized.Reason.SIGN_OUT))
                } else {
                    onNewMessage(Message.OpenAuthScreen)
                }
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            is Action.GetLink -> getLink(action.path, ::onNewMessage)
        }
    }

    private suspend fun getLink(path: HyperskillUrlPath, onNewMessage: (Message) -> Unit): Unit =
        urlPathProcessor.processUrlPath(path).fold(
            onSuccess = { url ->
                onNewMessage(Message.LinkReceived(url))
            },
            onFailure = {
                onNewMessage(Message.LinkReceiveFailed)
            }
        )
}
