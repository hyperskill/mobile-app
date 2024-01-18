package org.hyperskill.app.purchase.domain.model

import org.hyperskill.app.purchases.domain.model.PurchaseManager
import org.hyperskill.app.purchases.domain.model.PurchaseResult

// TODO: ALTAPPS-1110
class iOSPurchaseManager : PurchaseManager {
    override fun setup() {
        TODO("Not yet implemented")
    }

    override fun login(userId: Long) {
        TODO("Not yet implemented")
    }

    override fun logout() {
        TODO("Not yet implemented")
    }

    override suspend fun purchase(productId: String): Result<PurchaseResult> {
        TODO("Not yet implemented")
    }

    override suspend fun getManagementUrl(): Result<String?> {
        TODO("Not yet implemented")
    }
}