package org.hyperskill.app.subscriptions.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SubscriptionType {
    @SerialName("personal")
    PERSONAL,
    @SerialName("commercial")
    COMMERCIAL,
    @SerialName("team member")
    TEAM_MEMBER,
    @SerialName("trial")
    TRIAL,
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

    @SerialName("unknown")
    UNKNOWN
}