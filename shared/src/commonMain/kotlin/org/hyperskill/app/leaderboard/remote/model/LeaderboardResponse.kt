package org.hyperskill.app.leaderboard.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.leaderboard.domain.model.LeaderboardItem

@Serializable
class LeaderboardResponse(
    @SerialName("day")
    val dailyLeaderboard: List<LeaderboardItem>,
    @SerialName("week")
    val weeklyLeaderboard: List<LeaderboardItem>
)