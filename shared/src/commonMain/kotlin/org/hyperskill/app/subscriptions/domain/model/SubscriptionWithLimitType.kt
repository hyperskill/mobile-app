package org.hyperskill.app.subscriptions.domain.model

data class SubscriptionWithLimitType(
    val subscription: Subscription,
    val subscriptionLimitType: SubscriptionLimitType
)

val SubscriptionWithLimitType.isProblemsLimitReached: Boolean
    get() = subscriptionLimitType == SubscriptionLimitType.PROBLEMS && subscription.stepsLimitLeft == 0