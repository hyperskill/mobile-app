package org.hyperskill.app.purchases.domain.interactor

import org.hyperskill.app.purchases.domain.model.PlatformProductIdentifiers
import org.hyperskill.app.purchases.domain.model.PlatformPurchaseParams
import org.hyperskill.app.purchases.domain.model.PurchaseManager
import org.hyperskill.app.purchases.domain.model.PurchaseResult

class PurchaseInteractor(
    private val purchaseManager: PurchaseManager
) {
    /**
     * Identifies user in the payment sdk with provided [userId].
     * Must be called just after login event.
     */
    suspend fun login(userId: Long): Result<Unit> =
        if (!purchaseManager.isConfigured()) {
            runCatching {
                purchaseManager.configure(userId)
            }
        } else {
            purchaseManager.login(userId)
        }

    suspend fun purchaseMobileOnlySubscription(
        platformPurchaseParams: PlatformPurchaseParams
    ): Result<PurchaseResult> =
        purchaseManager.purchase(PlatformProductIdentifiers.MOBILE_ONLY_SUBSCRIPTION, platformPurchaseParams)

    suspend fun getManagementUrl(): Result<String?> =
        purchaseManager.getManagementUrl()

    suspend fun getFormattedMobileOnlySubscriptionPrice(): Result<String?> =
        purchaseManager.getFormattedProductPrice(PlatformProductIdentifiers.MOBILE_ONLY_SUBSCRIPTION)
}