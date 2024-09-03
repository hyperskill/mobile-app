package org.hyperskill.app.purchases.domain.interactor

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.domain.model.AnalyticKeys
import org.hyperskill.app.purchases.domain.model.PlatformProductIdentifiers
import org.hyperskill.app.purchases.domain.model.PlatformPurchaseParams
import org.hyperskill.app.purchases.domain.model.PurchaseManager
import org.hyperskill.app.purchases.domain.model.PurchaseResult

class PurchaseInteractor(
    private val purchaseManager: PurchaseManager,
    private val analyticInteractor: AnalyticInteractor
) {
    /**
     * Identifies user in the payment sdk with provided [userId].
     * Must be called just after login event.
     */
    suspend fun login(userId: Long): Result<Unit> {
        val result = if (!purchaseManager.isConfigured()) {
            runCatching {
                purchaseManager.configure(userId)
            }
        } else {
            purchaseManager.login(userId)
        }
        if (result.isSuccess) {
            analyticInteractor.setUserProperty(
                AnalyticKeys.CAN_MAKE_PAYMENTS,
                canMakePayments().getOrDefault(false)
            )
        }
        return result
    }

    suspend fun canMakePayments(): Result<Boolean> =
        purchaseManager.canMakePayments()

    suspend fun purchaseMobileOnlySubscription(
        platformPurchaseParams: PlatformPurchaseParams
    ): Result<PurchaseResult> =
        purchaseManager.purchase(PlatformProductIdentifiers.MOBILE_ONLY_MONTHLY_SUBSCRIPTION, platformPurchaseParams)

    suspend fun getManagementUrl(): Result<String?> =
        purchaseManager.getManagementUrl()

    suspend fun getFormattedMobileOnlySubscriptionPrice(): Result<String?> =
        purchaseManager.getFormattedProductPrice(PlatformProductIdentifiers.MOBILE_ONLY_MONTHLY_SUBSCRIPTION)

    suspend fun checkTrialEligibilityForMobileOnlySubscription(): Boolean =
        purchaseManager.checkTrialEligibility(PlatformProductIdentifiers.MOBILE_ONLY_MONTHLY_SUBSCRIPTION)
}