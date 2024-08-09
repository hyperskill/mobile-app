package org.hyperskill.app.core.presentation

import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import ru.nobird.app.presentation.redux.dispatcher.ActionDispatcher

/**
 * Base class for ActionDispatcher dispatching actions, that should not be cancelled with feature cancellation.
 * E.g. analytic event logging.
 *
 * @param coroutineScope is not cancelled on [cancel].
 * Instead it is complete to wait for all launched coroutines to finish.
 */
internal abstract class CompletableCoroutineActionDispatcher<Action, Message>(
    private val coroutineScope: CoroutineScope
) : ActionDispatcher<Action, Message> {

    private var isCancelled: Boolean = false

    abstract suspend fun handleNonCancellableAction(action: Action)

    override fun handleAction(action: Action) {
        if (isCancelled) return

        coroutineScope.launch {
            handleNonCancellableAction(action)
        }
    }

    override fun setListener(listener: (message: Message) -> Unit) {
        // no op
    }

    override fun cancel() {
        isCancelled = true
        (coroutineScope.coroutineContext[Job] as? CompletableJob)?.complete()
    }

    /**
     * Represents [CoroutineScope] config for logging [AnalyticEvent].
     * If [parentScope] is not presented uses a [Dispatchers.Main].immediate + [SupervisorJob].
     */
    interface ScopeConfigOptions {
        val parentScope: CoroutineScope?

        val coroutineExceptionHandler: CoroutineExceptionHandler

        fun createScope(): CoroutineScope {
            val parentScope = parentScope
            return if (parentScope != null) {
                parentScope + SupervisorJob(parentScope.coroutineContext[Job]) + coroutineExceptionHandler
            } else {
                CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate + coroutineExceptionHandler)
            }
        }
    }
}