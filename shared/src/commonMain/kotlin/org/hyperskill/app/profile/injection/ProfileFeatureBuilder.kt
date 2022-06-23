package org.hyperskill.app.profile.injection

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.profile.presentation.ProfileActionDispatcher
import org.hyperskill.app.profile.presentation.ProfileFeature
import org.hyperskill.app.profile.presentation.ProfileReducer
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature
import org.hyperskill.app.profile.presentation.ProfileFeature.State
import org.hyperskill.app.profile.presentation.ProfileFeature.Action
import org.hyperskill.app.profile.presentation.ProfileFeature.Message

object ProfileFeatureBuilder {
    fun build(profileInteractor: ProfileInteractor): Feature<State, Message, Action> {
        val profileReducer = ProfileReducer()
        val profileActionDispatcher = ProfileActionDispatcher(ActionDispatcherOptions(), profileInteractor)

        return ReduxFeature(ProfileFeature.State.Idle, profileReducer)
            .wrapWithActionDispatcher(profileActionDispatcher)
    }
}