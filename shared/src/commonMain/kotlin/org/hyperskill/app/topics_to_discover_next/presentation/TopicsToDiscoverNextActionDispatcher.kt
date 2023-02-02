package org.hyperskill.app.topics_to_discover_next.presentation

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.topics_repetitions.domain.flow.TopicRepeatedFlow
import org.hyperskill.app.topics_to_discover_next.domain.interactor.TopicsToDiscoverNextInteractor
import org.hyperskill.app.topics_to_discover_next.presentation.TopicsToDiscoverNextFeature.Action
import org.hyperskill.app.topics_to_discover_next.presentation.TopicsToDiscoverNextFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class TopicsToDiscoverNextActionDispatcher(
    config: ActionDispatcherOptions,
    private val topicsToDiscoverNextInteractor: TopicsToDiscoverNextInteractor,
    private val sentryInteractor: SentryInteractor,
    private val analyticInteractor: AnalyticInteractor,
    topicRepeatedFlow: TopicRepeatedFlow
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    init {
        topicRepeatedFlow.observe()
            .onEach { onNewMessage(Message.TopicRepeated(it)) }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchTopicsToDiscoverNext -> {
                sentryInteractor.startTransaction(action.sentryTransaction)

                val message = topicsToDiscoverNextInteractor
                    .getTopicsToDiscoverNext()
                    .fold(
                        onSuccess = {
                            sentryInteractor.finishTransaction(action.sentryTransaction)
                            Message.FetchTopicsToDiscoverNextSuccess(it)
                        },
                        onFailure = {
                            sentryInteractor.finishTransaction(action.sentryTransaction, throwable = it)
                            Message.FetchTopicsToDiscoverNextError
                        }
                    )

                onNewMessage(message)
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {
                // no op
            }
        }
    }
}