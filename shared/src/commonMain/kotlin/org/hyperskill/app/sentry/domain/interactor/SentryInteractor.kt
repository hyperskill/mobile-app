package org.hyperskill.app.sentry.domain.interactor

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.hyperskill.app.sentry.domain.model.breadcrumb.HyperskillSentryBreadcrumb
import org.hyperskill.app.sentry.domain.model.level.HyperskillSentryLevel
import org.hyperskill.app.sentry.domain.model.manager.SentryManager
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransaction

/**
 * Interactor that capable of performing business-logic with Sentry.
 *
 * @property sentryManager The platform specific implementation of the Sentry SDK.
 *
 * @see SentryManager
 */
class SentryInteractor(
    private val sentryManager: SentryManager
) {
    private val transactionsMutex = Mutex()

    fun setup() {
        sentryManager.setup()
    }

    fun addBreadcrumb(breadcrumb: HyperskillSentryBreadcrumb) {
        sentryManager.addBreadcrumb(breadcrumb)
    }

    fun captureMessage(message: String, level: HyperskillSentryLevel) {
        sentryManager.captureMessage(message, level)
    }

    fun captureErrorMessage(message: String) {
        sentryManager.captureErrorMessage(message)
    }

    fun setUsedId(userId: Long) {
        sentryManager.setUsedId(userId.toString())
    }

    fun clearCurrentUser() {
        sentryManager.clearCurrentUser()
    }

    suspend fun startTransaction(transaction: HyperskillSentryTransaction) {
        transactionsMutex.withLock {
            if (!sentryManager.containsOngoingTransaction(transaction)) {
                sentryManager.startTransaction(transaction)
            }
        }
    }

    suspend fun finishTransaction(transaction: HyperskillSentryTransaction, throwable: Throwable? = null) {
        transactionsMutex.withLock {
            sentryManager.finishTransaction(transaction, throwable)
        }
    }
}