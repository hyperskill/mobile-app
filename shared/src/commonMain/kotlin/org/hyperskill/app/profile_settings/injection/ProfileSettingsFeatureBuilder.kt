package org.hyperskill.app.profile_settings.injection

import kotlinx.coroutines.flow.MutableSharedFlow
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsActionDispatcher
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsReducer
import org.hyperskill.app.profile_settings.domain.interactor.ProfileSettingsInteractor
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object ProfileSettingsFeatureBuilder {
    fun build(profileSettingsInteractor: ProfileSettingsInteractor, profileInteractor: ProfileInteractor, authorizationFlow: MutableSharedFlow<UserDeauthorized>): Feature<ProfileSettingsFeature.State, ProfileSettingsFeature.Message, ProfileSettingsFeature.Action> {
        val profileSettingsReducer = ProfileSettingsReducer()
        val profileSettingsActionDispatcher =
            ProfileSettingsActionDispatcher(ActionDispatcherOptions(), profileSettingsInteractor, profileInteractor, authorizationFlow)

        return ReduxFeature(ProfileSettingsFeature.State.Idle, profileSettingsReducer)
            .wrapWithActionDispatcher(profileSettingsActionDispatcher)
    }
}