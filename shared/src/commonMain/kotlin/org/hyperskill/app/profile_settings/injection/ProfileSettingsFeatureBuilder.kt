package org.hyperskill.app.profile_settings.injection

import kotlinx.coroutines.flow.MutableSharedFlow
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.notification.domain.NotificationInteractor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.profile_settings.domain.interactor.ProfileSettingsInteractor
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsActionDispatcher
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Action
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Message
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.State
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsReducer
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object ProfileSettingsFeatureBuilder {
    fun build(
        profileSettingsInteractor: ProfileSettingsInteractor,
        profileInteractor: ProfileInteractor,
        notificationInteractor: NotificationInteractor,
        authorizationFlow: MutableSharedFlow<UserDeauthorized>
    ): Feature<State, Message, Action> {
        val profileSettingsReducer = ProfileSettingsReducer()
        val profileSettingsActionDispatcher =
            ProfileSettingsActionDispatcher(
                ActionDispatcherOptions(),
                profileSettingsInteractor,
                profileInteractor,
                authorizationFlow,
                notificationInteractor
            )

        return ReduxFeature(State.Idle, profileSettingsReducer)
            .wrapWithActionDispatcher(profileSettingsActionDispatcher)
    }
}