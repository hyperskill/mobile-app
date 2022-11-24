package org.hyperskill.app.home.injection

import kotlinx.coroutines.flow.SharedFlow
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.home.domain.interactor.HomeInteractor
import org.hyperskill.app.home.presentation.HomeActionDispatcher
import org.hyperskill.app.home.presentation.HomeFeature.Action
import org.hyperskill.app.home.presentation.HomeFeature.Message
import org.hyperskill.app.home.presentation.HomeFeature.State
import org.hyperskill.app.home.presentation.HomeReducer
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.streak.domain.interactor.StreakInteractor
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object HomeFeatureBuilder {
    fun build(
        analyticInteractor: AnalyticInteractor,
        homeInteractor: HomeInteractor,
        streakInteractor: StreakInteractor,
        profileInteractor: ProfileInteractor,
        stepInteractor: StepInteractor,
        topicRepeatedSharedFlow: SharedFlow<Unit>
    ): Feature<State, Message, Action> {
        val homeReducer = HomeReducer()
        val homeActionDispatcher = HomeActionDispatcher(
            ActionDispatcherOptions(),
            homeInteractor,
            streakInteractor,
            profileInteractor,
            stepInteractor,
            analyticInteractor,
            topicRepeatedSharedFlow
        )

        return ReduxFeature(State.Idle, homeReducer)
            .wrapWithActionDispatcher(homeActionDispatcher)
    }
}