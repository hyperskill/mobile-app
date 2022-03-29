package org.hyperskill.app.login.presentation

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.login.domain.interactor.UserLoginInteractor
import org.hyperskill.app.login.presentation.UserLoginFeature.Action
import org.hyperskill.app.login.presentation.UserLoginFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class UserLoginDispatcher(
    config: ActionDispatcherOptions,
    private val userListInteractor: UserLoginInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig())
