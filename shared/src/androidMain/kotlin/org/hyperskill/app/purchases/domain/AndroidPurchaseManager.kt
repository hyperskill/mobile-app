package org.hyperskill.app.purchases.domain

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
import com.revenuecat.purchases.awaitOfferings
import com.revenuecat.purchases.awaitPurchase
import com.revenuecat.purchases.models.StoreProduct
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import org.hyperskill.app.BuildConfig
import org.hyperskill.app.purchases.domain.model.PlatformPurchaseParams
import org.hyperskill.app.purchases.domain.model.PurchaseManager
import org.hyperskill.app.purchases.domain.model.PurchaseResult
import org.hyperskill.app.purchases.domain.model.SubscriptionOption
import org.hyperskill.app.purchases.domain.model.SubscriptionPeriod
import org.hyperskill.app.purchases.domain.model.SubscriptionProduct

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
        subscriptionOption: SubscriptionOption,
        platformPurchaseParams: PlatformPurchaseParams
    ): Result<PurchaseResult> =
        runCatching {
            val activity = platformPurchaseParams.activity
            try {
                val purchaseResult = Purchases.sharedInstance.awaitPurchase(
                    PurchaseParams.Builder(activity, subscriptionOption.revenueCatSubscriptionOption).build()
                )
                PurchaseResult.Succeed(
                    orderId = purchaseResult.storeTransaction.orderId,
                    productIds = purchaseResult.storeTransaction.productIds
                )
            } catch (e: PurchasesTransactionException) {
                mapException(e)
            }
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
            fetchProduct(listOf(productId)).firstOrNull()?.price?.formatted
        }

    override suspend fun getSubscriptionProducts(): Result<List<SubscriptionProduct>> =
        kotlin.runCatching {
            val currentOffering = Purchases.sharedInstance.awaitOfferings().current
                ?: return@runCatching emptyList()
            currentOffering.availablePackages.mapNotNull {
                val product = it.product
                SubscriptionProduct(
                    id = product.id,
                    period = when (product.period?.unit) {
                        com.revenuecat.purchases.models.Period.Unit.MONTH -> SubscriptionPeriod.MONTH
                        com.revenuecat.purchases.models.Period.Unit.YEAR -> SubscriptionPeriod.YEAR
                        else -> return@mapNotNull null
                    },
                    formattedPrice = product.price.formatted,
                    formattedPricePerMonth = product.formattedPricePerMonth() ?: return@mapNotNull null,
                    isTrialEligible = false,
                    subscriptionOption = SubscriptionOption(
                        requireNotNull(product.subscriptionOptions).first()
                    )
                )
            }
        }

    override suspend fun checkTrialEligibility(productId: String): Boolean = false

    private suspend fun fetchProduct(productIds: List<String>): List<StoreProduct> =
        Purchases.sharedInstance.awaitGetProducts(productIds)
}