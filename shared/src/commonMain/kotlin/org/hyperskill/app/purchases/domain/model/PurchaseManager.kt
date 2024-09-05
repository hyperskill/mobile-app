package org.hyperskill.app.purchases.domain.model

/**
 * Represents an interface that both platforms should implement.
 */
interface PurchaseManager {

    fun isConfigured(): Boolean

    /**
     * Setups the payment sdk with provided [userId]
     */
    fun configure(userId: Long)

    /**
     * Identifies user in the payment sdk with provided [userId].
     * Must be called just after login event.
     */
    suspend fun login(userId: Long): Result<Unit>

    suspend fun canMakePayments(): Result<Boolean>

    /**
     * Makes purchase of the product with [productId].
     */
    suspend fun purchase(
        subscriptionOption: SubscriptionOption,
        platformPurchaseParams: PlatformPurchaseParams
    ): Result<PurchaseResult>

    suspend fun getManagementUrl(): Result<String?>

    /**
     * Returns formatted product price with currency by [productIds]
     */
    suspend fun getFormattedProductPrice(productId: String): Result<String?>

    suspend fun getSubscriptionProducts(): Result<List<SubscriptionProduct>>

    /**
     * Checks if user is eligible for trial for the product with [productId]
     */
    suspend fun checkTrialEligibility(productId: String): Boolean
}