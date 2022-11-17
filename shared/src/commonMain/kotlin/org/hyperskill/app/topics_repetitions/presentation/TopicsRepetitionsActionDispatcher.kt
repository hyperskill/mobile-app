package org.hyperskill.app.topics_repetitions.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.Action
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.Message
import org.hyperskill.app.topics.domain.interactor.TopicsInteractor
import org.hyperskill.app.topics_repetitions.domain.interactor.TopicsRepetitionsInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class TopicsRepetitionsActionDispatcher(
    config: ActionDispatcherOptions,
    private val topicsRepetitionsInteractor: TopicsRepetitionsInteractor,
    private val topicsInteractor: TopicsInteractor,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchNextTopics -> {}
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
        }
    }
}