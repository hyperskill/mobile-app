package org.hyperskill.app.topics_repetitions.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.Action
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.Message
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.State
import org.hyperskill.app.topics.domain.interactor.TopicsInteractor
import org.hyperskill.app.topics_repetitions.domain.interactor.TopicsRepetitionsInteractor
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsActionDispatcher
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsReducer
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object TopicsRepetitionsFeatureBuilder {
    fun build(
        topicsRepetitionsInteractor: TopicsRepetitionsInteractor,
        topicsInteractor: TopicsInteractor,
        analyticInteractor: AnalyticInteractor
    ): Feature<State, Message, Action> {
        val topicsRepetitionsReducer = TopicsRepetitionsReducer()

        val topicsRepetitionsActionDispatcher = TopicsRepetitionsActionDispatcher(
            ActionDispatcherOptions(),
            topicsRepetitionsInteractor,
            topicsInteractor,
            analyticInteractor
        )

        return ReduxFeature(State.Idle, topicsRepetitionsReducer)
            .wrapWithActionDispatcher(topicsRepetitionsActionDispatcher)
    }
}