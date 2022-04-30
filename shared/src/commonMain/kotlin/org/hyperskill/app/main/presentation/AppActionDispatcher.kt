package org.hyperskill.app.main.presentation

import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.main.presentation.AppFeature.Action
import org.hyperskill.app.main.presentation.AppFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class AppActionDispatcher(
    config: ActionDispatcherOptions,
    private val authInteractor: AuthInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    init {
        authInteractor
            .observeUserDeauthorization()
            .onEach { _ -> onNewMessage(Message.UserDeauthorized) }
            .launchIn(actionScope)
    }
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            Action.DetermineUserAccountStatus -> {
                val result = authInteractor.isAuthorized()

                val message =
                    result
                        .map { isAuthorized -> Message.UserAccountStatus(isAuthorized) }
                        .getOrElse { Message.UserAccountStatus(isAuthorized = false) }
                onNewMessage(message)
            }
        }
    }
}