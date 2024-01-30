package org.hyperskill.app.purchases.domain.interactor

import org.hyperskill.app.purchases.domain.model.PlatformPurchaseParams
import org.hyperskill.app.purchases.domain.model.PurchaseManager
import org.hyperskill.app.purchases.domain.model.PurchaseResult

class PurchaseInteractor(
    private val purchaseManager: PurchaseManager
) {
    companion object {
        private const val MOBILE_ONLY_SUBSCRIPTION_PRODUCT_ID: String = "premium_mobile"
    }

    fun setup() {
        purchaseManager.setup()
    }

    /**
     * Identifies user in the payment sdk with provided [userId].
     * Must be called just after login event.
     */
    suspend fun login(userId: Long): Result<Unit> =
        purchaseManager.login(userId)

    /**
     * Clear the user identification provided via [login].
     * Must be called just after logout event.
     */
    suspend fun logout(): Result<Unit> =
        purchaseManager.logout()

    suspend fun purchaseMobileOnlySubscription(
        platformPurchaseParams: PlatformPurchaseParams
    ): Result<PurchaseResult> =
        purchaseManager.purchase(MOBILE_ONLY_SUBSCRIPTION_PRODUCT_ID, platformPurchaseParams)

    suspend fun getManagementUrl(): Result<String?> =
        purchaseManager.getManagementUrl()
}