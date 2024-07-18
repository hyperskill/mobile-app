package org.hyperskill.app.subscriptions.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
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
    val status: SubscriptionStatus = SubscriptionStatus.ACTIVE,
    @SerialName("steps_limit_total")
    val stepsLimitTotal: Int? = null,
    @SerialName("steps_limit_left")
    val stepsLimitLeft: Int? = null,
    @SerialName("steps_limit_reset_time")
    val stepsLimitResetTime: Instant? = null,
    @SerialName("valid_till")
    val validTill: Instant? = null
)

internal fun Subscription.getProblemsLimitType(
    isMobileContentTrialEnabled: Boolean
): ProblemsLimitType =
    when (type) {
        SubscriptionType.MOBILE_ONLY -> if (isActive) {
            type.problemsLimitType
        } else {
            if (isMobileContentTrialEnabled) ProblemsLimitType.FIXED else ProblemsLimitType.DAILY
        }
        else -> type.problemsLimitType
    }

internal fun Subscription.isProblemsLimitReached(
    isMobileContentTrialEnabled: Boolean
): Boolean {
    val problemsLimitType = getProblemsLimitType(isMobileContentTrialEnabled)
    return problemsLimitType == ProblemsLimitType.DAILY && stepsLimitLeft == 0
}

internal val Subscription.isFreemium: Boolean
    get() = type == SubscriptionType.FREEMIUM ||
        type == SubscriptionType.MOBILE_ONLY && status != SubscriptionStatus.ACTIVE

fun Subscription.orContentTrial(
    isMobileContentTrialEnabled: Boolean
): Subscription =
    if (type == SubscriptionType.FREEMIUM && isMobileContentTrialEnabled) {
        Subscription(type = SubscriptionType.MOBILE_CONTENT_TRIAL)
    } else {
        this
    }

internal val Subscription.isActive: Boolean
    get() = status == SubscriptionStatus.ACTIVE

internal val Subscription.isExpired: Boolean
    get() = status == SubscriptionStatus.EXPIRED

internal fun Subscription.isValidTillPassed(): Boolean =
    if (validTill != null) {
        val nowByUTC = Clock.System.now()
            .toLocalDateTime(TimeZone.UTC)
            .toInstant(TimeZone.UTC)
        validTill < nowByUTC
    } else {
        false
    }