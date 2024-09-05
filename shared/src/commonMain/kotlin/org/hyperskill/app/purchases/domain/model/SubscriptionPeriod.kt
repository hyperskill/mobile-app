package org.hyperskill.app.purchases.domain.model

enum class SubscriptionPeriod {
    MONTH,
    YEAR
}

fun SubscriptionPeriod.toProductIdentifier(): String =
    when (this) {
        SubscriptionPeriod.MONTH -> PlatformProductIdentifiers.MOBILE_ONLY_MONTHLY_SUBSCRIPTION
        SubscriptionPeriod.YEAR -> PlatformProductIdentifiers.MOBILE_ONLY_YEARLY_SUBSCRIPTION
    }