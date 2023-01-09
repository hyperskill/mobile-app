package org.hyperskill.app.home.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.extension.PluralsFormatter
import org.hyperskill.app.home.domain.interactor.HomeInteractor
import org.hyperskill.app.home.presentation.HomeActionDispatcher
import org.hyperskill.app.home.presentation.HomeFeature.Action
import org.hyperskill.app.home.presentation.HomeFeature.Message
import org.hyperskill.app.home.presentation.HomeFeature.State
import org.hyperskill.app.home.presentation.HomeReducer
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.streak.domain.interactor.StreakInteractor
import org.hyperskill.app.topics_repetitions.domain.interactor.TopicsRepetitionsInteractor
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object HomeFeatureBuilder {
    fun build(
        homeInteractor: HomeInteractor,
        streakInteractor: StreakInteractor,
        profileInteractor: ProfileInteractor,
        topicsRepetitionsInteractor: TopicsRepetitionsInteractor,
        stepInteractor: StepInteractor,
        analyticInteractor: AnalyticInteractor,
        sentryInteractor: SentryInteractor,
        urlPathProcessor: UrlPathProcessor,
        pluralsFormatter: PluralsFormatter
    ): Feature<State, Message, Action> {
        val homeReducer = HomeReducer()
        val homeActionDispatcher = HomeActionDispatcher(
            ActionDispatcherOptions(),
            homeInteractor,
            streakInteractor,
            profileInteractor,
            topicsRepetitionsInteractor,
            stepInteractor,
            analyticInteractor,
            sentryInteractor,
            urlPathProcessor,
            pluralsFormatter
        )

        return ReduxFeature(State.Idle, homeReducer)
            .wrapWithActionDispatcher(homeActionDispatcher)
    }
}