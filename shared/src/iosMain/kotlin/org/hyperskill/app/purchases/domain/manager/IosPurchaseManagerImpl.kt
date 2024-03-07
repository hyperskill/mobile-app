package org.hyperskill.app.purchases.domain.manager

import org.hyperskill.app.purchases.domain.model.PlatformPurchaseParams
import org.hyperskill.app.purchases.domain.model.PurchaseManager
import org.hyperskill.app.purchases.domain.model.PurchaseResult

internal class IosPurchaseManagerImpl(
    private val purchases: IosPurchaseManager
) : PurchaseManager {
    override fun isConfigured(): Boolean =
        purchases.isConfigured()

    override fun configure(userId: Long) {
        purchases.configure(userId)
    }

    override suspend fun login(userId: Long): Result<Unit> =
        purchases
            .login(userId)
            .toKotlinResult()

    override suspend fun purchase(
        productId: String,
        platformPurchaseParams: PlatformPurchaseParams
    ): Result<PurchaseResult> =
        purchases
            .purchase(productId, platformPurchaseParams)
            .toKotlinResult()

    override suspend fun getManagementUrl(): Result<String?> =
        purchases
            .getManagementUrl()
            .toKotlinResult()
            .map { if (it?.isEmpty() == true) null else it }

    override suspend fun getFormattedProductPrice(productId: String): Result<String?> =
        Result.success(purchases.getFormattedProductPrice(productId))
}