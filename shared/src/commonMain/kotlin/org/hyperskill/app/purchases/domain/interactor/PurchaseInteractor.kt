package org.hyperskill.app.purchases.domain.interactor

import org.hyperskill.app.purchases.domain.model.PurchaseManager
import org.hyperskill.app.purchases.domain.model.PurchaseResult

class PurchaseInteractor(
    private val purchaseManager: PurchaseManager
) {
    fun setup() {
        purchaseManager.setup()
    }

    /**
     * Identifies user in the payment sdk with provided [userId].
     * Must be called just after login event.
     */
    fun login(userId: Long) {
        purchaseManager.login(userId)
    }

    /**
     * Clear the user identification provided via [login].
     * Must be called just after logout event.
     */
    fun logout() {
        purchaseManager.logout()
    }

    suspend fun purchaseMobileOnlySubscription(): Result<PurchaseResult> =
        purchaseManager.purchase("premium_mobile")

    suspend fun getManagementUrl(): Result<String?> =
        purchaseManager.getManagementUrl()
}