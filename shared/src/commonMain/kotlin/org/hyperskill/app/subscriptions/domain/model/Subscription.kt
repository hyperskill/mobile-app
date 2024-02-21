package org.hyperskill.app.subscriptions.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.subscriptions.cache.CurrentSubscriptionStateHolderImpl

/**
 * Represents a user subscription.
 *
 * Warning!
 * This model is stored in the cache.
 * Adding new field or modifying old ones,
 * check that all fields will be deserialized from cache without an error.
 * All the new optional fields must have default values.
 * @see [CurrentSubscriptionStateHolderImpl]
 *
 * If the [stepsLimitResetTime] is null, then the user doesn't have submissions yet
 */
@Serializable
data class Subscription(
    @SerialName("type")
    val type: SubscriptionType = SubscriptionType.UNKNOWN,
    val status: SubscriptionStatus,
    @SerialName("steps_limit_total")
    val stepsLimitTotal: Int?,
    @SerialName("steps_limit_left")
    val stepsLimitLeft: Int?,
    @SerialName("steps_limit_reset_time")
    val stepsLimitResetTime: Instant?,
    @SerialName("valid_till")
    val validTill: Instant?
)

internal val Subscription.areProblemsLimited: Boolean
    get() = when (type) {
        SubscriptionType.MOBILE_ONLY -> type.areProblemsLimited || status != SubscriptionStatus.ACTIVE
        else -> type.areProblemsLimited
    }

internal val Subscription.isProblemLimitReached: Boolean
    get() = areProblemsLimited && stepsLimitLeft == 0

internal val Subscription.isFreemium: Boolean
    get() = type == SubscriptionType.FREEMIUM ||
        type == SubscriptionType.MOBILE_ONLY && status != SubscriptionStatus.ACTIVE

internal val Subscription.isActive: Boolean
    get() = status == SubscriptionStatus.ACTIVE

internal val Subscription.isExpired: Boolean
    get() = status == SubscriptionStatus.EXPIRED

internal val Subscription.isValidTillPassed: Boolean
    get() = if (validTill != null) {
        validTill < Clock.System.now()
    } else {
        false
    }