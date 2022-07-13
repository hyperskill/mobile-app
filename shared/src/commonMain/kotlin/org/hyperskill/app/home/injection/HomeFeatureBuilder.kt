package org.hyperskill.app.home.injection

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.home.presentation.HomeActionDispatcher
import org.hyperskill.app.home.presentation.HomeReducer
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.step.domain.interactor.StepInteractor
import ru.nobird.app.presentation.redux.feature.Feature
import org.hyperskill.app.home.presentation.HomeFeature.Action
import org.hyperskill.app.home.presentation.HomeFeature.Message
import org.hyperskill.app.home.presentation.HomeFeature.State
import org.hyperskill.app.streak.domain.interactor.StreakInteractor
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object HomeFeatureBuilder {
    fun build(streakInteractor: StreakInteractor, profileInteractor: ProfileInteractor, stepInteractor: StepInteractor): Feature<State, Message, Action> {
        val homeReducer = HomeReducer()
        val homeActionDispatcher = HomeActionDispatcher(ActionDispatcherOptions(), streakInteractor, profileInteractor, stepInteractor)

        return ReduxFeature(State.Idle, homeReducer)
            .wrapWithActionDispatcher(homeActionDispatcher)
    }
}