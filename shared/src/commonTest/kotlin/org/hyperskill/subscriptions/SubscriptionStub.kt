package org.hyperskill.subscriptions

import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.model.SubscriptionType

fun Subscription.Companion.stub(
    type: SubscriptionType = SubscriptionType.PREMIUM
): Subscription =
    Subscription(
        type = type,
        stepsLimitLeft = null,
        stepsLimitTotal = null,
        stepsLimitResetTime = null,
        validTill = null
    )