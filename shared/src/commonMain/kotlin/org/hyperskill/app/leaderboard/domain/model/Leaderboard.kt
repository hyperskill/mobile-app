package org.hyperskill.app.leaderboard.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Leaderboard(
    @SerialName("day")
    val day: List<LeaderboardItem>,
    @SerialName("week")
    val week: List<LeaderboardItem>
)