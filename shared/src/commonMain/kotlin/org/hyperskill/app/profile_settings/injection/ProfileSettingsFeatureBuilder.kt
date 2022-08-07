package org.hyperskill.app.profile_settings.injection

import org.hyperskill.app.auth.domain.interactor.AuthInteractor
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
    fun build(profileSettingsInteractor: ProfileSettingsInteractor, authInteractor: AuthInteractor, profileInteractor: ProfileInteractor): Feature<ProfileSettingsFeature.State, ProfileSettingsFeature.Message, ProfileSettingsFeature.Action> {
        val profileSettingsReducer = ProfileSettingsReducer()
        val profileSettingsActionDispatcher =
            ProfileSettingsActionDispatcher(ActionDispatcherOptions(), profileSettingsInteractor, authInteractor, profileInteractor)

        return ReduxFeature(ProfileSettingsFeature.State.Idle, profileSettingsReducer)
            .wrapWithActionDispatcher(profileSettingsActionDispatcher)
    }
}