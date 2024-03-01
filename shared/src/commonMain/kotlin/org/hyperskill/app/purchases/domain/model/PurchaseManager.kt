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

    /**
     * Makes purchase of the product with [productId].
     */
    suspend fun purchase(
        productId: String,
        platformPurchaseParams: PlatformPurchaseParams
    ): Result<PurchaseResult>

    suspend fun getManagementUrl(): Result<String?>

    /**
     * Returns formatted product price with currency by [productId]
     */
    suspend fun getFormattedProductPrice(productId: String): Result<String?>
}