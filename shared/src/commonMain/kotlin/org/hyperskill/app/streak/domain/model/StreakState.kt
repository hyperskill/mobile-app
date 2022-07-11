package org.hyperskill.app.streak.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class StreakState {
    @SerialName("completed")
    COMPLETED,
    @SerialName("frozen")
    FROZEN,
    @SerialName("manual_completed")
    MANUAL_COMPLETED,
    @SerialName("nothing")
    NOTHING
}