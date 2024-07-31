package org.hyperskill.app.subscriptions.domain.interactor

import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.model.SubscriptionLimitType

data class SubscriptionWithLimitType(
    val subscription: Subscription,
    val limitType: SubscriptionLimitType
)