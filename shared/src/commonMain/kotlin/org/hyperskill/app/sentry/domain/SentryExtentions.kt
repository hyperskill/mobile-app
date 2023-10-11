package org.hyperskill.app.sentry.domain

import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransaction

/**
 * Execute a block of code within a Sentry transaction.
 *
 * @param transaction The transaction to be started.
 * @param onError Callback function to create a result in case of an error.
 * @param measureBlock The block of code to be executed within the transaction.
 * @return The measureBlock execution result.
 */
suspend inline fun <T> SentryInteractor.withTransaction(
    transaction: HyperskillSentryTransaction,
    onError: (Throwable) -> T,
    measureBlock: () -> T
): T {
    startTransaction(transaction)
    return try {
        val result = measureBlock()
        finishTransaction(transaction)
        result
    } catch (e: Exception) {
        finishTransaction(transaction, e)
        onError(e)
    }
}