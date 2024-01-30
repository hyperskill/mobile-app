package org.hyperskill.app.purchases.domain.model

sealed interface PurchaseResult {
    data class Succeed(
        val orderId: String?,
        val productIds: List<String>
    ) : PurchaseResult

    object CancelledByUser : PurchaseResult

    sealed interface Error : PurchaseResult {

        val message: String

        val underlyingErrorMessage: String?

        class ErrorWhileFetchingProduct(
            val productId: String,
            val originMessage: String,
            override val underlyingErrorMessage: String?
        ) : Error {
            override val message: String
                get() = "Error while fetching product with id=$productId"
        }

        class NoProductFound(
            val productId: String
        ) : Error {
            override val message: String
                get() = "Can't find product with id=$productId"

            override val underlyingErrorMessage: String?
                get() = null
        }

        /**
         * The receipt is already in use by another subscriber.
         * Log in with the previous account or contact support
         * to get your purchases transferred to regain access.
         */
        class ReceiptAlreadyInUseError(
            override val message: String,
            override val underlyingErrorMessage: String?
        ) : Error

        /**
         * The purchase is pending and may be completed at a later time.
         * This can happen when awaiting parental approval or going
         * through extra authentication flows for credit cards in some countries.
         */
        class PaymentPendingError(
            override val message: String,
            override val underlyingErrorMessage: String?
        ) : Error

        /**
         * Subscription is already purchased. Log in with the account
         * that originally performed this purchase if you're using a different one.
         */
        class ProductAlreadyPurchasedError(
            override val message: String,
            override val underlyingErrorMessage: String?
        ) : Error

        /**
         * Purchasing wasn't allowed, which is common if the card is declined
         * or the purchase is not available in the country
         * you're trying to purchase from.
         */
        class PurchaseNotAllowedError(
            override val message: String,
            override val underlyingErrorMessage: String?
        ) : Error

        /**
         * There was a problem with the Google Play Store.
         * This is a generic Google error, and there's not enough information
         * to determine the cause.
         */
        class StoreProblemError(
            override val message: String,
            override val underlyingErrorMessage: String?
        ) : Error

        /**
         * Some other kind of error.
         * For example, configuration error or invalid user id error.
         */
        data class OtherError(
            override val message: String,
            override val underlyingErrorMessage: String?
        ) : Error
    }
}