package org.hyperskill.app.purchases.domain

import android.app.Activity
import android.app.Application
import com.revenuecat.purchases.LogLevel
import com.revenuecat.purchases.PurchaseParams
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import com.revenuecat.purchases.PurchasesErrorCode
import com.revenuecat.purchases.PurchasesException
import com.revenuecat.purchases.PurchasesTransactionException
import com.revenuecat.purchases.awaitCustomerInfo
import com.revenuecat.purchases.awaitGetProducts
import com.revenuecat.purchases.awaitLogIn
import com.revenuecat.purchases.awaitPurchase
import com.revenuecat.purchases.models.StoreProduct
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import org.hyperskill.app.BuildConfig
import org.hyperskill.app.purchases.domain.model.PlatformPurchaseParams
import org.hyperskill.app.purchases.domain.model.PurchaseManager
import org.hyperskill.app.purchases.domain.model.PurchaseResult

class AndroidPurchaseManager(
    private val application: Application,
    private val isDebugMode: Boolean
) : PurchaseManager {

    override fun isConfigured(): Boolean =
        Purchases.isConfigured

    override fun configure(userId: Long) {
        Purchases.logLevel = if (isDebugMode) LogLevel.DEBUG else LogLevel.INFO
        Purchases.configure(
            PurchasesConfiguration
                .Builder(
                    context = application,
                    apiKey = BuildConfig.REVENUE_CAT_GOOGLE_API_KEY
                )
                .appUserID(userId.toString())
                .build()
        )
    }

    override suspend fun login(userId: Long): Result<Unit> =
        kotlin.runCatching {
            Purchases.sharedInstance.awaitLogIn(userId.toString())
        }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun canMakePayments(): Result<Boolean> =
        runCatching {
            suspendCoroutine { continuation ->
                try {
                    Purchases.canMakePayments(application) { result ->
                        continuation.resume(result)
                    }
                } catch (e: Exception) {
                    continuation.resumeWithException(e)
                }
            }
        }

    override suspend fun purchase(
        productId: String,
        platformPurchaseParams: PlatformPurchaseParams
    ): Result<PurchaseResult> =
        runCatching {
            val product = try {
                fetchProduct(productId) ?: return@runCatching PurchaseResult.Error.NoProductFound(productId)
            } catch (e: PurchasesException) {
                return@runCatching mapProductFetchException(productId, e)
            }
            val activity = platformPurchaseParams.activity
            purchase(activity, product)
        }

    private fun mapProductFetchException(productId: String, e: PurchasesException): PurchaseResult =
        PurchaseResult.Error.ErrorWhileFetchingProduct(
            productId = productId,
            originMessage = e.message,
            underlyingErrorMessage = e.error.underlyingErrorMessage
        )

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
                PurchaseResult.Error.ReceiptAlreadyInUseError(e.message, e.underlyingErrorMessage)
            PurchasesErrorCode.PaymentPendingError ->
                PurchaseResult.Error.PaymentPendingError(e.message, e.underlyingErrorMessage)
            PurchasesErrorCode.ProductAlreadyPurchasedError ->
                PurchaseResult.Error.ProductAlreadyPurchasedError(e.message, e.underlyingErrorMessage)
            PurchasesErrorCode.PurchaseNotAllowedError ->
                PurchaseResult.Error.PurchaseNotAllowedError(e.message, e.underlyingErrorMessage)
            PurchasesErrorCode.StoreProblemError ->
                PurchaseResult.Error.StoreProblemError(e.message, e.underlyingErrorMessage)
            else -> PurchaseResult.Error.OtherError(e.message, e.underlyingErrorMessage)
        }
    }

    override suspend fun getManagementUrl(): Result<String?> =
        kotlin.runCatching {
            Purchases.sharedInstance.awaitCustomerInfo().managementURL?.toString()
        }

    override suspend fun getFormattedProductPrice(productId: String): Result<String?> =
        kotlin.runCatching {
            fetchProduct(productId)?.price?.formatted
        }

    private suspend fun fetchProduct(productId: String): StoreProduct? =
        Purchases.sharedInstance
            .awaitGetProducts(listOf(productId))
            .firstOrNull()
}