package org.hyperskill.app.search.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.search.domain.interactor.SearchInteractor
import org.hyperskill.app.search.presentation.SearchFeature.Action
import org.hyperskill.app.search.presentation.SearchFeature.InternalAction
import org.hyperskill.app.search.presentation.SearchFeature.InternalMessage
import org.hyperskill.app.search.presentation.SearchFeature.Message
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class SearchActionDispatcher(
    config: ActionDispatcherOptions,
    private val searchInteractor: SearchInteractor,
    private val sentryInteractor: SentryInteractor,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.PerformSearch -> {
                handlePerformSearchAction(action, ::onNewMessage)
            }
            is InternalAction.LogAnalyticEvent -> {
                analyticInteractor.logEvent(action.analyticEvent)
            }
        }
    }

    private suspend fun handlePerformSearchAction(
        action: InternalAction.PerformSearch,
        onNewMessage: (Message) -> Unit
    ) {
        sentryInteractor.withTransaction(
            HyperskillSentryTransactionBuilder.buildSearchFeaturePerformSearch(),
            onError = { InternalMessage.PerformSearchError }
        ) {
            val topics = searchInteractor
                .searchTopics(query = action.query)
                .getOrThrow()
            InternalMessage.PerformSearchSuccess(
                topics = topics
            )
        }.let(onNewMessage)
    }
}