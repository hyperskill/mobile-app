package org.hyperskill.app.purchase.domain.model

import org.hyperskill.app.purchases.domain.model.PlatformPurchaseParams
import org.hyperskill.app.purchases.domain.model.PurchaseManager
import org.hyperskill.app.purchases.domain.model.PurchaseResult

// TODO: ALTAPPS-1110
class iOSPurchaseManager : PurchaseManager {
    override fun setup() {
        TODO("Not yet implemented")
    }

    override suspend fun login(userId: Long): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun logout(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun purchase(
        productId: String,
        platformPurchaseParams: PlatformPurchaseParams
    ): Result<PurchaseResult> {
        TODO("Not yet implemented")
    }

    override suspend fun getManagementUrl(): Result<String?> {
        TODO("Not yet implemented")
    }
}