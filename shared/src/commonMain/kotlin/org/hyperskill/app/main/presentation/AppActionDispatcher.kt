package org.hyperskill.app.main.presentation

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.main.presentation.AppFeature.Action
import org.hyperskill.app.main.presentation.AppFeature.Message
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class AppActionDispatcher(
    config: ActionDispatcherOptions,
    private val authInteractor: AuthInteractor,
    private val profileInteractor: ProfileInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    init {
        authInteractor
            .observeUserDeauthorization()
            .onEach {
                authInteractor.clearCache()
                onNewMessage(Message.UserDeauthorized(it.reason))
            }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.DetermineUserAccountStatus -> {
                val profileResult = profileInteractor.getCurrentProfile(sourceType = DataSourceType.REMOTE)

                val message =
                    profileResult
                        .map { profile -> Message.UserAccountStatus(profile) }
                        .getOrElse { Message.UserAccountStatusError }

                onNewMessage(message)
            }
        }
    }
}