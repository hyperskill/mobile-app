package org.hyperskill.app.subscriptions.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SubscriptionType(
    val isProjectSelectionEnabled: Boolean = false,
    val isProjectInfoAvailable: Boolean = true,
    val isCertificateAvailable: Boolean = true,
    val areHintsLimited: Boolean = false,
    val isDailyProblemLimitEnabled: Boolean = false,
    val subscriptionLimitType: SubscriptionLimitType = SubscriptionLimitType.NONE
) {
    @SerialName("personal")
    PERSONAL(isProjectSelectionEnabled = true),
    @SerialName("commercial")
    COMMERCIAL,
    @SerialName("team member")
    TEAM_MEMBER,
    @SerialName("trial")
    TRIAL(isProjectSelectionEnabled = true),
    @SerialName("content trial")
    CONTENT_TRIAL,
    @SerialName("organization trial")
    ORGANIZATION_TRIAL,
    @SerialName("hyperskill team")
    HYPERSKILL_TEAM,
    @SerialName("Hyperskill Staff")
    HYPERSKILL_STAFF,
    @SerialName("stepik team")
    STEPIK_TEAM,
    @SerialName("jetbrains team")
    JETBRAINS_TEAM,
    @SerialName("free")
    FREE,

    @SerialName("freemium")
    FREEMIUM(
        isCertificateAvailable = false,
        isProjectInfoAvailable = false,
        areHintsLimited = true,
        subscriptionLimitType = SubscriptionLimitType.PROBLEMS
    ),
    MOBILE_CONTENT_TRIAL(
        isCertificateAvailable = false,
        isProjectInfoAvailable = false,
        areHintsLimited = true,
        subscriptionLimitType = SubscriptionLimitType.TOPICS
    ),

    @SerialName("mobile only")
    MOBILE_ONLY(
        isCertificateAvailable = false,
        isProjectInfoAvailable = false,
        areHintsLimited = true
    ),

    @SerialName("premium")
    PREMIUM(isProjectSelectionEnabled = true),
    @SerialName("paused premium")
    PAUSED_PREMIUM(
        isProjectInfoAvailable = false,
        isCertificateAvailable = false,
        areHintsLimited = true,
        subscriptionLimitType = SubscriptionLimitType.PROBLEMS
    ),

    @SerialName("unknown")
    UNKNOWN
}