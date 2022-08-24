package org.hyperskill.app.profile_settings.presentation

import kotlinx.coroutines.flow.MutableSharedFlow
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.notification.domain.NotificationInteractor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.profile_settings.domain.interactor.ProfileSettingsInteractor
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Action
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class ProfileSettingsActionDispatcher(
    config: ActionDispatcherOptions,
    private val profileSettingsInteractor: ProfileSettingsInteractor,
    private val profileInteractor: ProfileInteractor,
    private val authorizationFlow:  MutableSharedFlow<UserDeauthorized>,
    private val notificationInteractor: NotificationInteractor
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
            is Action.Logout -> {
                profileInteractor.clearCache()
                notificationInteractor.clearAskUserToEnableDailyRemindersInfo()
                authorizationFlow.tryEmit(UserDeauthorized)
            }
        }
    }
}