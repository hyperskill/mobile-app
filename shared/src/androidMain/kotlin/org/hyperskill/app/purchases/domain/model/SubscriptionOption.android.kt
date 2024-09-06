package org.hyperskill.app.purchases.domain.model

import com.revenuecat.purchases.models.SubscriptionOption

actual class HyperskillStoreProduct(
    val revenueCatSubscriptionOption: SubscriptionOption
)