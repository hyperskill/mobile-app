package org.hyperskill.app.purchases.domain.model

data class SubscriptionProduct(
    val id: String,
    val period: SubscriptionPeriod,
    val formattedPrice: String,
    val formattedPricePerMonth: String,
    val isTrialEligible: Boolean,
    val storeProduct: HyperskillStoreProduct
)
