package org.hyperskill.app.profile.presentation

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.interactor.ProfileSettingsInteractor
import org.hyperskill.app.profile.presentation.ProfileSettingsFeature.Action
import org.hyperskill.app.profile.presentation.ProfileSettingsFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class ProfileSettingsActionDispatcher(
    config: ActionDispatcherOptions,
    private val profileSettingsInteractor: ProfileSettingsInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchProfileSettings -> {
                val profileSettings = profileSettingsInteractor
                    .getProfileSettings()
                    .getOrElse {
                        onNewMessage(Message.ProfileSettingsError)
                        return
                    }
                onNewMessage(Message.ProfileSettingsSuccess(profileSettings))
            }
        }
    }
}