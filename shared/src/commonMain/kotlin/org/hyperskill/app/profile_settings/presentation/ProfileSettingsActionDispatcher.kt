package org.hyperskill.app.profile_settings.presentation

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile_settings.domain.interactor.ProfileSettingsInteractor
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Action
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class ProfileSettingsActionDispatcher(
    config: ActionDispatcherOptions,
    private val profileSettingsInteractor: ProfileSettingsInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchProfileSettings -> {
                val profileSettings = profileSettingsInteractor.getProfileSettings()
                onNewMessage(Message.ProfileSettingsSuccess(profileSettings))
            }
            is Action.ChangeTheme -> {
                profileSettingsInteractor.changeTheme(action.theme)
            }
        }
    }
}