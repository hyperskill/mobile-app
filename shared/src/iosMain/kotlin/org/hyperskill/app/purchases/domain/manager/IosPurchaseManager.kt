package org.hyperskill.app.purchases.domain.manager

import org.hyperskill.app.core.domain.model.SwiftyResult
import org.hyperskill.app.purchases.domain.model.PlatformPurchaseParams
import org.hyperskill.app.purchases.domain.model.PurchaseResult

interface IosPurchaseManager {
    fun isConfigured(): Boolean

    fun configure(userId: Long)

    suspend fun login(userId: Long): SwiftyResult<Unit, Throwable>

    suspend fun purchase(
        productId: String,
        platformPurchaseParams: PlatformPurchaseParams
    ): SwiftyResult<PurchaseResult, Throwable>

    suspend fun getManagementUrl(): SwiftyResult<String?, Throwable>

    suspend fun getFormattedProductPrice(productId: String): String?
}