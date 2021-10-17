package org.hyperskill.app.user_list.presentation

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.user_list.domain.interactor.UserListInteractor
import org.hyperskill.app.user_list.presentation.UsersListFeature.Action
import org.hyperskill.app.user_list.presentation.UsersListFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class UsersListDispatcher(
    config: ActionDispatcherOptions,
    private val userListInteractor: UserListInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchUsers -> {
                val result = userListInteractor.getUsers(action.usersQuery.userName.orEmpty())

                val message =
                    result
                        .map { Message.UsersLoaded.Success(it) }
                        .getOrElse {
                            Message.UsersLoaded.Error(errorMsg = it.message ?: "")
                        }

                onNewMessage(message)
            }
        }
    }
}