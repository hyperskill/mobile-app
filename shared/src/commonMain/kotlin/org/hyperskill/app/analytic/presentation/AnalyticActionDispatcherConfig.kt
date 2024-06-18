package org.hyperskill.app.analytic.presentation

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import org.hyperskill.app.core.domain.throwError

internal class AnalyticActionDispatcherConfig(
    override val logAnalyticParentScope: CoroutineScope? = null
) : AnalyticActionDispatcher.ScopeConfigOptions {
    override val logAnalyticCoroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            if (throwable !is CancellationException) {
                throwError(throwable) // rethrow if not cancellation exception
            }
        }
}