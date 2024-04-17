package org.hyperskill.subscriptions

import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.model.SubscriptionStatus
import org.hyperskill.app.subscriptions.domain.model.SubscriptionType

fun Subscription.Companion.stub(
    type: SubscriptionType = SubscriptionType.PREMIUM,
    status: SubscriptionStatus = SubscriptionStatus.ACTIVE,
    stepsLimitLeft: Int? = null,
    stepsLimitTotal: Int? = null
): Subscription =
    Subscription(
        type = type,
        status = status,
        stepsLimitLeft = stepsLimitLeft,
        stepsLimitTotal = stepsLimitTotal,
        stepsLimitResetTime = null,
        validTill = null
    )