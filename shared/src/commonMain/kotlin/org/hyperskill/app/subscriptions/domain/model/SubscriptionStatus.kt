package org.hyperskill.app.subscriptions.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SubscriptionStatus {
    @SerialName("pending")
    PENDING,
    @SerialName("not_active")
    NOT_ACTIVE,
    @SerialName("active")
    ACTIVE,
    @SerialName("expired")
    EXPIRED
}