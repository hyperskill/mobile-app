package org.hyperskill.app.core.presentation

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import org.hyperskill.app.core.domain.throwError

internal class CompletableCoroutineActionDispatcherConfig(
    override val parentScope: CoroutineScope? = null
) : CompletableCoroutineActionDispatcher.ScopeConfigOptions {
    override val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            if (throwable !is CancellationException) {
                throwError(throwable) // rethrow if not cancellation exception
            }
        }
}