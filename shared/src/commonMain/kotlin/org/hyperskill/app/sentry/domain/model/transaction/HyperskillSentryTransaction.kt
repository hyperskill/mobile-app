package org.hyperskill.app.sentry.domain.model.transaction

/**
 * Represents a Sentry transaction info.
 *
 * @property name The transaction name.
 * @property operation The operation
 */
open class HyperskillSentryTransaction(
    val name: String,
    val operation: String
) {
    constructor(
        name: String,
        operation: HyperskillSentryTransactionOperation
    ) : this(name, operation.stringValue)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HyperskillSentryTransaction) return false

        if (name != other.name) return false
        if (operation != other.operation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + operation.hashCode()
        return result
    }
}
