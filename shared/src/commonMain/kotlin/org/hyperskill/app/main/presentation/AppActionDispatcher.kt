package org.hyperskill.app.main.presentation

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.main.presentation.AppFeature.Action
import org.hyperskill.app.main.presentation.AppFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class AppActionDispatcher(
    config: ActionDispatcherOptions
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
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