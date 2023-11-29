package org.hyperskill.app.leaderboard.data.source

import org.hyperskill.app.leaderboard.remote.model.LeaderboardResponse

interface LeaderboardRemoteDataSource {
    suspend fun getLeaderboard(): Result<LeaderboardResponse>
}