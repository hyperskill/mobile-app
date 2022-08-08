package org.hyperskill.app.profile_settings.injection

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
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
    fun build(profileSettingsInteractor: ProfileSettingsInteractor): Feature<State, Message, Action> {
        val profileSettingsReducer = ProfileSettingsReducer()
        val profileSettingsActionDispatcher =
            ProfileSettingsActionDispatcher(ActionDispatcherOptions(), profileSettingsInteractor)

        return ReduxFeature(State.Idle, profileSettingsReducer)
            .wrapWithActionDispatcher(profileSettingsActionDispatcher)
    }
}