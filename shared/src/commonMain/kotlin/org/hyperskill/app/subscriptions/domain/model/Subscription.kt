package org.hyperskill.app.subscriptions.domain.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * If the [stepsLimitResetTime] is null, then the user doesn't have submissions yet
 */
@Serializable
data class Subscription(
    @SerialName("type")
    val type: SubscriptionType = SubscriptionType.UNKNOWN,
    @SerialName("steps_limit_total")
    val stepsLimitTotal: Int?,
    @SerialName("steps_limit_left")
    val stepsLimitLeft: Int?,
    @SerialName("steps_limit_reset_time")
    val stepsLimitResetTime: Instant?
)

val Subscription.isProblemLimitReached: Boolean
    get() = type.areProblemsLimited && stepsLimitLeft == 0

val Subscription.isFreemium: Boolean
    get() = type == SubscriptionType.FREEMIUM