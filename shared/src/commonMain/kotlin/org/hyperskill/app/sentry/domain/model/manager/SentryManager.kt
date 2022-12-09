package org.hyperskill.app.sentry.domain.model.manager

import org.hyperskill.app.sentry.domain.model.breadcrumb.HyperskillSentryBreadcrumb
import org.hyperskill.app.sentry.domain.model.level.HyperskillSentryLevel
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransaction

/**
 * Represents a protocol that both platforms should implement.
 */
interface SentryManager {
    /**
     * Setups the Sentry SDK.
     * Must be called when the application is launched for the first time.
     */
    fun setup()

    /**
     * Adds a breadcrumb to the current Scope.
     *
     * @param breadcrumb The corresponding breadcrumb to add to the current Scope.
     * @see HyperskillSentryBreadcrumb
     */
    fun addBreadcrumb(breadcrumb: HyperskillSentryBreadcrumb)

    /**
     * Captures the message.
     *
     * @param message The message to send.
     * @param level The message level.
     */
    fun captureMessage(message: String, level: HyperskillSentryLevel)

    /**
     * Captures the message with the error level.
     *
     * @param message The message to send.
     */
    fun captureErrorMessage(message: String) {
        captureMessage(message, HyperskillSentryLevel.ERROR)
    }

    /**
     * Sets the id of the user.
     *
     * @param userId the user id.
     */
    fun setUsedId(userId: String)

    /**
     *  Clears the currently set user.
     */
    fun clearCurrentUser()

    /**
     * Checks for the specified transaction.
     *
     * @param transaction The transaction to search for.
     * @return Returns true if contains the specified transaction.
     * @see HyperskillSentryTransaction
     */
    fun containsOngoingTransaction(transaction: HyperskillSentryTransaction): Boolean

    /**
     * Creates a transaction, started transaction is set on the scope.
     *
     * @param transaction The transaction info used to create platform Sentry transaction.
     * @see HyperskillSentryTransaction
     */
    fun startTransaction(transaction: HyperskillSentryTransaction)

    /**
     * Finishes the transaction by setting the end time and status.
     *
     * @param transaction The transaction info.
     * @param throwable The throwable that was thrown during the execution of the transaction.
     * @see HyperskillSentryTransaction
     */
    fun finishTransaction(transaction: HyperskillSentryTransaction, throwable: Throwable? = null)
}