package org.hyperskill.app.leaderboard.domain.repository

import org.hyperskill.app.leaderboard.remote.model.LeaderboardResponse

interface LeaderboardRepository {
    suspend fun getLeaderboard(): Result<LeaderboardResponse>
}