package org.hyperskill.app.purchases.domain

import android.app.Activity
import android.app.Application
import com.revenuecat.purchases.LogLevel
import com.revenuecat.purchases.PurchaseParams
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import com.revenuecat.purchases.PurchasesErrorCode
import com.revenuecat.purchases.PurchasesTransactionException
import com.revenuecat.purchases.awaitCustomerInfo
import com.revenuecat.purchases.awaitGetProducts
import com.revenuecat.purchases.awaitLogIn
import com.revenuecat.purchases.awaitLogOut
import com.revenuecat.purchases.awaitPurchase
import com.revenuecat.purchases.models.StoreProduct
import java.lang.ref.WeakReference
import org.hyperskill.app.BuildConfig
import org.hyperskill.app.purchases.domain.model.PurchaseManager
import org.hyperskill.app.purchases.domain.model.PurchaseResult

class AndroidPurchaseManager(
    private val application: Application,
    private val activityRef: WeakReference<Activity>,
    private val isDebugMode: Boolean
) : PurchaseManager {
    override fun setup() {
        if (!Purchases.isConfigured) {
            Purchases.logLevel = if (isDebugMode) LogLevel.DEBUG else LogLevel.INFO
            Purchases.configure(
                PurchasesConfiguration.Builder(
                    context = application,
                    apiKey = BuildConfig.REVENUE_CAT_GOOGLE_API_KEY
                ).build()
            )
        }
    }

    override suspend fun login(userId: Long): Result<Unit> =
        kotlin.runCatching {
            Purchases.sharedInstance.awaitLogIn(userId.toString())
        }

    override suspend fun logout(): Result<Unit> =
        runCatching {
            Purchases.sharedInstance.awaitLogOut()
        }

    override suspend fun purchase(productId: String): Result<PurchaseResult> {
        val product = Purchases.sharedInstance
            .awaitGetProducts(listOf(productId))
            .firstOrNull()
        if (product == null) {
            return Result.success(PurchaseResult.Error.NoProductFound(productId))
        }
        val activity = activityRef.get()
        return if (activity == null) {
            Result.success(
                PurchaseResult
                    .DidNotStart(message = "Can't initiate a purchase. Activity is dead.")
            )
        } else {
            kotlin.runCatching {
                purchase(activity, product)
            }
        }
    }

    private suspend fun purchase(activity: Activity, product: StoreProduct): PurchaseResult =
        try {
            val purchaseResult = Purchases.sharedInstance.awaitPurchase(
                PurchaseParams.Builder(activity, product).build()
            )
            PurchaseResult.Succeed(
                orderId = purchaseResult.storeTransaction.orderId,
                productIds = purchaseResult.storeTransaction.productIds
            )
        } catch (e: PurchasesTransactionException) {
            mapException(e)
        }

    private fun mapException(e: PurchasesTransactionException): PurchaseResult {
        if (e.userCancelled) return PurchaseResult.CancelledByUser
        return when (e.error.code) {
            PurchasesErrorCode.ReceiptAlreadyInUseError ->
                PurchaseResult.Error.ReceiptAlreadyInUseError(e.error.message, e.code.description)
            PurchasesErrorCode.PaymentPendingError ->
                PurchaseResult.Error.PaymentPendingError(e.error.message, e.code.description)
            PurchasesErrorCode.ProductAlreadyPurchasedError ->
                PurchaseResult.Error.ProductAlreadyPurchasedError(e.error.message, e.code.description)
            PurchasesErrorCode.PurchaseNotAllowedError ->
                PurchaseResult.Error.PurchaseNotAllowedError(e.error.message, e.code.description)
            PurchasesErrorCode.StoreProblemError ->
                PurchaseResult.Error.StoreProblemError(e.error.message, e.code.description)
            else -> PurchaseResult.Error.OtherError(e.error.message, e.code.description)
        }
    }

    override suspend fun getManagementUrl(): Result<String?> =
        kotlin.runCatching {
            Purchases.sharedInstance.awaitCustomerInfo().managementURL?.toString()
        }
}