package org.hyperskill.app.profile.injection

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.interactor.ProfileSettingsInteractor
import org.hyperskill.app.profile.presentation.ProfileSettingsActionDispatcher
import org.hyperskill.app.profile.presentation.ProfileSettingsFeature.Action
import org.hyperskill.app.profile.presentation.ProfileSettingsFeature.Message
import org.hyperskill.app.profile.presentation.ProfileSettingsFeature.State
import org.hyperskill.app.profile.presentation.ProfileSettingsReducer
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object ProfileSettingsFeatureBuilder {
    fun build(profileSettingsInteractor: ProfileSettingsInteractor): Feature<State, Message, Action> {
        val profileSettingsReducer = ProfileSettingsReducer()
        val profileSettingsActionDispatcher = ProfileSettingsActionDispatcher(ActionDispatcherOptions(), profileSettingsInteractor)

        return ReduxFeature(State.Idle, profileSettingsReducer)
            .wrapWithActionDispatcher(profileSettingsActionDispatcher)
    }
}