package org.hyperskill.app.home.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.home.domain.interactor.HomeInteractor
import org.hyperskill.app.home.presentation.HomeActionDispatcher
import org.hyperskill.app.home.presentation.HomeFeature.Action
import org.hyperskill.app.home.presentation.HomeFeature.Message
import org.hyperskill.app.home.presentation.HomeFeature.State
import org.hyperskill.app.home.presentation.HomeReducer
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
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
        urlPathProcessor: UrlPathProcessor
    ): Feature<State, Message, Action> {
        val homeReducer = HomeReducer()
        val homeActionDispatcher = HomeActionDispatcher(
            ActionDispatcherOptions(),
            homeInteractor,
            streakInteractor,
            profileInteractor,
            stepInteractor,
            analyticInteractor,
            urlPathProcessor
        )

        return ReduxFeature(State.Idle, homeReducer)
            .wrapWithActionDispatcher(homeActionDispatcher)
    }
}