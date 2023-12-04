package org.hyperskill.app.leaderboard.domain.repository

import org.hyperskill.app.leaderboard.domain.model.Leaderboard

interface LeaderboardRepository {
    suspend fun getLeaderboard(): Result<Leaderboard>
}