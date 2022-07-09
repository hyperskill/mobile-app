package org.hyperskill.app.streak.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HistoricalStreak(
    @SerialName("date")
    val date: String,
    @SerialName("state")
    val state: StreakState?,
    @SerialName("target_type")
    val targetType: String,
    @SerialName("target_id")
    val targetId: Long?
)
