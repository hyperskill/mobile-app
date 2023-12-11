package org.hyperskill.app.search.presentation

import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    private var searchJob: Job? = null

    companion object {
        private val SEARCH_DELAY = 500.toDuration(DurationUnit.MILLISECONDS)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.PerformSearch -> {
                handlePerformSearchAction(action, ::onNewMessage)
            }
            InternalAction.CancelSearch -> {
                searchJob?.cancel()
            }
            is InternalAction.LogAnalyticEvent -> {
                analyticInteractor.logEvent(action.analyticEvent)
            }
            else -> {
                // no op
            }
        }
    }

    private suspend fun handlePerformSearchAction(
        action: InternalAction.PerformSearch,
        onNewMessage: (Message) -> Unit
    ) {
        suspend fun search() {
            sentryInteractor.withTransaction(
                HyperskillSentryTransactionBuilder.buildSearchFeaturePerformSearch(),
                onError = { InternalMessage.PerformSearchError }
            ) {
                val topics = searchInteractor
                    .searchTopics(query = action.query)
                    .getOrThrow()
                InternalMessage.PerformSearchSuccess(topics)
            }.let(onNewMessage)
        }

        searchJob?.cancel()

        if (action.withDelay) {
            searchJob = actionScope.launch {
                delay(SEARCH_DELAY)
                search()
            }
        } else {
            search()
        }
    }
}