package org.hyperskill.app.main.presentation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.main.presentation.AppFeature.Action
import org.hyperskill.app.main.presentation.AppFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class AppActionDispatcher(
    config: ActionDispatcherOptions,
    authorizationFlow: MutableSharedFlow<Message>
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    init {
        authorizationFlow
            .onEach { message ->
                onNewMessage(message)
            }
            .launchIn(actionScope)
    }
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            Action.DetermineUserAccountStatus -> {
                val isAuthorized = false
                val newMessage = if (isAuthorized) Message.UserAuthorized else Message.UserDeauthorized
                onNewMessage(newMessage)
            }
        }
    }
}