package org.hyperskill.app.profile_settings.injection

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsActionDispatcher
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsReducer
import org.hyperskill.app.profile_settings.domain.interactor.ProfileSettingsInteractor
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object ProfileSettingsFeatureBuilder {
    fun build(profileSettingsInteractor: ProfileSettingsInteractor): Feature<ProfileSettingsFeature.State, ProfileSettingsFeature.Message, ProfileSettingsFeature.Action> {
        val profileSettingsReducer = ProfileSettingsReducer()
        val profileSettingsActionDispatcher =
            ProfileSettingsActionDispatcher(ActionDispatcherOptions(), profileSettingsInteractor)

        return ReduxFeature(ProfileSettingsFeature.State.Idle, profileSettingsReducer)
            .wrapWithActionDispatcher(profileSettingsActionDispatcher)
    }
}