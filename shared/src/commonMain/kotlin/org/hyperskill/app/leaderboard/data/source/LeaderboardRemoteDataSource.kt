package org.hyperskill.app.leaderboard.data.source

import org.hyperskill.app.leaderboard.domain.model.Leaderboard

interface LeaderboardRemoteDataSource {
    suspend fun getLeaderboard(): Result<Leaderboard>
}