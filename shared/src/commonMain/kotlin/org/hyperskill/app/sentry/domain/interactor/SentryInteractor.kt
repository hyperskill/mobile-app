package org.hyperskill.app.sentry.domain.interactor

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.AnalyticEventMonitor
import org.hyperskill.app.sentry.domain.model.breadcrumb.HyperskillSentryBreadcrumb
import org.hyperskill.app.sentry.domain.model.breadcrumb.HyperskillSentryBreadcrumbAnalyticEventMapper
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
) : AnalyticEventMonitor {
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

    fun captureErrorMessage(message: String, data: Map<String, Any> = emptyMap()) {
        sentryManager.captureErrorMessage(message, data)
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

    // Conforming to AnalyticEventMonitor

    override fun analyticDidReportEvent(event: AnalyticEvent) {
        val breadcrumb = HyperskillSentryBreadcrumbAnalyticEventMapper.mapAnalyticEvent(event)
        if (breadcrumb.message.isNotEmpty()) {
            addBreadcrumb(breadcrumb)
        }
    }

    override fun analyticDidFlushEvents() {
        // no op
    }
}