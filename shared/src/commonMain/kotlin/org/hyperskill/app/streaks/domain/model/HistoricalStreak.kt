package org.hyperskill.app.streaks.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HistoricalStreak(
    @SerialName("state")
    val state: StreakState? = null
) {
    val isCompleted: Boolean =
        state == StreakState.COMPLETED || state == StreakState.MANUAL_COMPLETED
}