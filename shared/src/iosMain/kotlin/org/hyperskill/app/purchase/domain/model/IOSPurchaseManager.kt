package org.hyperskill.app.purchase.domain.model

import org.hyperskill.app.purchases.domain.model.PlatformPurchaseParams
import org.hyperskill.app.purchases.domain.model.PurchaseManager
import org.hyperskill.app.purchases.domain.model.PurchaseResult

// TODO: ALTAPPS-1110
class IOSPurchaseManager : PurchaseManager {
    override fun setup() {}

    override suspend fun login(userId: Long): Result<Unit> =
        Result.failure(IllegalStateException("iOS platform not supports purchases"))

    override suspend fun purchase(
        productId: String,
        platformPurchaseParams: PlatformPurchaseParams
    ): Result<PurchaseResult> =
        Result.failure(IllegalStateException("iOS platform not supports purchases"))

    override suspend fun getManagementUrl(): Result<String?> =
        Result.failure(IllegalStateException("iOS platform not supports purchases"))

    override suspend fun getFormattedProductPrice(productId: String): Result<String?> =
        Result.failure(IllegalStateException("iOS platform not supports purchases"))
}