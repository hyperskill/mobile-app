package org.hyperskill.app.leaderboard.data.repository

import org.hyperskill.app.leaderboard.data.source.LeaderboardRemoteDataSource
import org.hyperskill.app.leaderboard.domain.repository.LeaderboardRepository
import org.hyperskill.app.leaderboard.remote.model.LeaderboardResponse

internal class LeaderboardRepositoryImpl(
    private val leaderboardRemoteDataSource: LeaderboardRemoteDataSource
) : LeaderboardRepository {
    override suspend fun getLeaderboard(): Result<LeaderboardResponse> =
        leaderboardRemoteDataSource.getLeaderboard()
}