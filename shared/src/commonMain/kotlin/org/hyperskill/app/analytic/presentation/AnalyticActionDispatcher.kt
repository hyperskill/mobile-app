package org.hyperskill.app.analytic.presentation

import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import ru.nobird.app.presentation.redux.dispatcher.ActionDispatcher

/**
 * Is responsible for dispatching analytic events.
 *
 * @property analyticInteractor the [AnalyticInteractor] used for logging analytic events
 * @property logAnalyticScope the [CoroutineScope] used for logging analytic events
 * @property getAnalyticEvent a function that takes an [Action] as input and returns a collection of [AnalyticEvent].
 * If the function returns null, no events will be logged.
 */
internal class AnalyticActionDispatcher<Action, Message>(
    private val analyticInteractor: AnalyticInteractor,
    private val logAnalyticScope: CoroutineScope,
    private val getAnalyticEvent: (Action) -> Collection<AnalyticEvent>?
) : ActionDispatcher<Action, Message> {

    private var isCancelled: Boolean = false

    override fun handleAction(action: Action) {
        if (isCancelled) return

        val analyticEvents = getAnalyticEvent(action)
        if (!analyticEvents.isNullOrEmpty()) {
            logAnalyticScope.launch {
                analyticEvents.forEach { analyticEvent ->
                    analyticInteractor.logEvent(analyticEvent)
                }
            }
        }
    }

    override fun setListener(listener: (message: Message) -> Unit) {
        // no op
    }

    override fun cancel() {
        isCancelled = true
        (logAnalyticScope.coroutineContext[Job] as? CompletableJob)?.complete()
    }

    /**
     * Represents [CoroutineScope] config for logging [AnalyticEvent].
     * If [logAnalyticParentScope] is not presented uses a [Dispatchers.Main].immediate + [SupervisorJob].
     */
    interface ScopeConfigOptions {
        val logAnalyticParentScope: CoroutineScope?

        val logAnalyticCoroutineExceptionHandler: CoroutineExceptionHandler

        fun createLogAnalyticScope(): CoroutineScope {
            val parentScope = logAnalyticParentScope
            return if (parentScope != null) {
                parentScope + SupervisorJob(parentScope.coroutineContext[Job]) + logAnalyticCoroutineExceptionHandler
            } else {
                CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate + logAnalyticCoroutineExceptionHandler)
            }
        }
    }
}