package org.hyperskill.app.analytic.presentation

import kotlinx.coroutines.CoroutineScope
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature

/**
 * Wraps the given [Feature] with an [AnalyticActionDispatcher].
 *
 * @param analyticInteractor The [AnalyticInteractor] used for logging analytic events.
 *
 * @param parentScope The parent [CoroutineScope] to use for creating the logAnalyticScope. The Default value is null.
 * @see [AnalyticActionDispatcher.ScopeConfigOptions] for more details.
 *
 * @param getAnalyticEvent A function that takes an [Action] as input and returns an [AnalyticEvent].
 * If the function returns null, no events will be logged.
 *
 * @see [AnalyticActionDispatcher], [AnalyticActionDispatcherConfig], [AnalyticActionDispatcher.ScopeConfigOptions]
 */
internal inline fun <State, Message, Action> Feature<State, Message, Action>.wrapWithAnalyticLogger(
    analyticInteractor: AnalyticInteractor,
    parentScope: CoroutineScope? = null,
    crossinline getAnalyticEvent: (Action) -> AnalyticEvent?
): Feature<State, Message, Action> =
    wrapWithActionDispatcher(
        AnalyticActionDispatcher(
            analyticInteractor = analyticInteractor,
            logAnalyticScope = AnalyticActionDispatcherConfig(parentScope).createLogAnalyticScope(),
        ) { action ->
            val event = getAnalyticEvent(action)
            if (event != null) listOf(event) else null
        }
    )

/**
 * Wraps the given [Feature] with an [AnalyticActionDispatcher].
 *
 * @param analyticInteractor The [AnalyticInteractor] used for logging analytic events.
 *
 * @param parentScope The parent CoroutineScope to use for creating the logAnalyticScope. The Default value is null.
 * @see [AnalyticActionDispatcher.ScopeConfigOptions] for more details.
 *
 * @param getAnalyticEvent A function that takes an [Action] as input and returns a collection of [AnalyticEvent].
 * If the function returns null, no events will be logged.
 *
 * @see [AnalyticActionDispatcher], [AnalyticActionDispatcherConfig], [AnalyticActionDispatcher.ScopeConfigOptions]
 */
internal inline fun <State, Message, Action> Feature<State, Message, Action>.wrapWithBatchAnalyticLogger(
    analyticInteractor: AnalyticInteractor,
    parentScope: CoroutineScope? = null,
    noinline getAnalyticEvent: (Action) -> Collection<AnalyticEvent>?
): Feature<State, Message, Action> =
    wrapWithActionDispatcher(
        AnalyticActionDispatcher(
            analyticInteractor = analyticInteractor,
            logAnalyticScope = AnalyticActionDispatcherConfig(parentScope).createLogAnalyticScope(),
            getAnalyticEvent = getAnalyticEvent
        )
    )