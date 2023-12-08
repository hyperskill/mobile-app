package org.hyperskill.app.leaderboard.data.repository

import org.hyperskill.app.leaderboard.data.source.LeaderboardRemoteDataSource
import org.hyperskill.app.leaderboard.domain.model.Leaderboard
import org.hyperskill.app.leaderboard.domain.repository.LeaderboardRepository

internal class LeaderboardRepositoryImpl(
    private val leaderboardRemoteDataSource: LeaderboardRemoteDataSource
) : LeaderboardRepository {
    override suspend fun getLeaderboard(): Result<Leaderboard> =
        leaderboardRemoteDataSource.getLeaderboard()
}