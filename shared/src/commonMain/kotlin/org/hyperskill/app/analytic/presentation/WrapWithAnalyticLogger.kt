package org.hyperskill.app.analytic.presentation

import kotlinx.coroutines.CoroutineScope
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.core.presentation.CompletableCoroutineActionDispatcher
import org.hyperskill.app.core.presentation.CompletableCoroutineActionDispatcherConfig
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature

/**
 * Wraps the given [Feature] with an [CompletableCoroutineActionDispatcher].
 *
 * @param analyticInteractor The [AnalyticInteractor] used for logging analytic events.
 *
 * @param parentScope The parent [CoroutineScope] to use for creating the logAnalyticScope. The Default value is null.
 * @see [CompletableCoroutineActionDispatcher.ScopeConfigOptions] for more details.
 *
 * @param getAnalyticEvent A function that takes an [Action] as input and returns an [AnalyticEvent].
 * If the function returns null, no events will be logged.
 *
 * @see [CompletableCoroutineActionDispatcher], [CompletableCoroutineActionDispatcherConfig], [CompletableCoroutineActionDispatcher.ScopeConfigOptions]
 */
internal inline fun <State, Message, Action> Feature<State, Message, Action>.wrapWithAnalyticLogger(
    analyticInteractor: AnalyticInteractor,
    parentScope: CoroutineScope? = null,
    crossinline getAnalyticEvent: (Action) -> AnalyticEvent?
): Feature<State, Message, Action> =
    wrapWithActionDispatcher(
        SingleAnalyticEventActionDispatcher(
            analyticInteractor = analyticInteractor,
            parentScope = parentScope,
            getAnalyticEvent = getAnalyticEvent
        )
    )

/**
 * Wraps the given [Feature] with an [CompletableCoroutineActionDispatcher].
 *
 * @param analyticInteractor The [AnalyticInteractor] used for logging analytic events.
 *
 * @param parentScope The parent CoroutineScope to use for creating the logAnalyticScope. The Default value is null.
 * @see [CompletableCoroutineActionDispatcher.ScopeConfigOptions] for more details.
 *
 * @param getAnalyticEvent A function that takes an [Action] as input and returns a collection of [AnalyticEvent].
 * If the function returns null, no events will be logged.
 *
 * @see [CompletableCoroutineActionDispatcher], [CompletableCoroutineActionDispatcherConfig], [CompletableCoroutineActionDispatcher.ScopeConfigOptions]
 */
internal fun <State, Message, Action> Feature<State, Message, Action>.wrapWithBatchAnalyticLogger(
    analyticInteractor: AnalyticInteractor,
    parentScope: CoroutineScope? = null,
    getAnalyticEvent: (Action) -> Collection<AnalyticEvent>?
): Feature<State, Message, Action> =
    wrapWithActionDispatcher(
        BatchAnalyticEventActionDispatcher(
            analyticInteractor = analyticInteractor,
            parentScope = parentScope,
            getAnalyticEvent = getAnalyticEvent
        )
    )

@Suppress("FunctionName")
internal inline fun <Action, Message> SingleAnalyticEventActionDispatcher(
    analyticInteractor: AnalyticInteractor,
    parentScope: CoroutineScope? = null,
    crossinline getAnalyticEvent: (Action) -> AnalyticEvent?
): CompletableCoroutineActionDispatcher<Action, Message> =
    object : CompletableCoroutineActionDispatcher<Action, Message>(
        coroutineScope = CompletableCoroutineActionDispatcherConfig(parentScope).createScope()
    ) {
        override suspend fun handleNonCancellableAction(action: Action) {
            val event = getAnalyticEvent(action)
            if (event != null) {
                analyticInteractor.logEvent(event)
            }
        }
    }

@Suppress("FunctionName", "unused")
internal inline fun <Action, Message> BatchAnalyticEventActionDispatcher(
    analyticInteractor: AnalyticInteractor,
    parentScope: CoroutineScope? = null,
    noinline getAnalyticEvent: (Action) -> Collection<AnalyticEvent>?
): CompletableCoroutineActionDispatcher<Action, Message> =
    object : CompletableCoroutineActionDispatcher<Action, Message>(
        coroutineScope = CompletableCoroutineActionDispatcherConfig(parentScope).createScope(),
    ) {
        override suspend fun handleNonCancellableAction(action: Action) {
            getAnalyticEvent(action)?.forEach { analyticEvent ->
                analyticInteractor.logEvent(analyticEvent)
            }
        }
    }