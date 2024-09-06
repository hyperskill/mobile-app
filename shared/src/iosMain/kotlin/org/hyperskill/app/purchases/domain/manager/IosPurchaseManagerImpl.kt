package org.hyperskill.app.purchases.domain.manager

import cocoapods.RevenueCat.RCOfferings
import cocoapods.RevenueCat.RCPackage
import cocoapods.RevenueCat.RCPurchases
import cocoapods.RevenueCat.localizedPricePerMonth
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import org.hyperskill.app.core.domain.model.toKotlinResult
import org.hyperskill.app.purchases.domain.model.HyperskillStoreProduct
import org.hyperskill.app.purchases.domain.model.PlatformPurchaseParams
import org.hyperskill.app.purchases.domain.model.PurchaseManager
import org.hyperskill.app.purchases.domain.model.PurchaseResult
import org.hyperskill.app.purchases.domain.model.SubscriptionPeriod
import org.hyperskill.app.purchases.domain.model.SubscriptionProduct

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

    override suspend fun canMakePayments(): Result<Boolean> =
        Result.success(true)

    override suspend fun purchase(
        storeProduct: HyperskillStoreProduct,
        platformPurchaseParams: PlatformPurchaseParams
    ): Result<PurchaseResult> =
        purchases
            .purchase(storeProduct.rcStoreProduct, platformPurchaseParams)
            .toKotlinResult()

    override suspend fun getManagementUrl(): Result<String?> =
        purchases
            .getManagementUrl()
            .toKotlinResult()
            .map { if (it?.isEmpty() == true) null else it }

    override suspend fun getSubscriptionProducts(): Result<List<SubscriptionProduct>> =
        kotlin.runCatching {
            suspendCoroutine { continuation ->
                RCPurchases.sharedPurchases().getOfferingsWithCompletion { rcOfferings, nsError ->
                    when {
                        nsError != null -> {
                            continuation.resumeWithException(FetchOfferingsException(nsError.description))
                        }
                        rcOfferings == null -> {
                            continuation.resumeWithException(FetchOfferingsException("Received rcOfferings is null"))
                        }
                        else -> {
                            continuation.resume(mapOfferingsToSubscriptionProducts(rcOfferings))
                        }
                    }
                }
            }
        }

    private fun mapOfferingsToSubscriptionProducts(rcOfferings: RCOfferings): List<SubscriptionProduct> {
        val currentOffering = rcOfferings.current() ?: return emptyList()
        return currentOffering
            .availablePackages()
            .mapNotNull {
                val _package = it as? RCPackage ?: return@mapNotNull null
                val rcStoreProduct = _package.storeProduct()
                SubscriptionProduct(
                    id = rcStoreProduct.productIdentifier(),
                    period = when (rcStoreProduct.subscriptionPeriod()?.unit()) {
                        cocoapods.RevenueCat.RCSubscriptionPeriodUnitMonth -> SubscriptionPeriod.MONTH
                        cocoapods.RevenueCat.RCSubscriptionPeriodUnitYear -> SubscriptionPeriod.YEAR
                        else -> return@mapNotNull null
                    },
                    formattedPrice = rcStoreProduct.localizedPriceString(),
                    formattedPricePerMonth = rcStoreProduct.localizedPricePerMonth() ?: return@mapNotNull null,
                    isTrialEligible = false,
                    storeProduct = HyperskillStoreProduct(rcStoreProduct)
                )
            }
    }

    override suspend fun checkTrialEligibility(productId: String): Boolean =
        purchases.checkTrialOrIntroDiscountEligibility(productId)
}

class FetchOfferingsException(override val message: String?) : Exception()