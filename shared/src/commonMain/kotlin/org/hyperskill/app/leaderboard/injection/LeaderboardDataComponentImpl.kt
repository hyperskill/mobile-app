package org.hyperskill.app.leaderboard.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.leaderboard.data.repository.LeaderboardRepositoryImpl
import org.hyperskill.app.leaderboard.data.source.LeaderboardRemoteDataSource
import org.hyperskill.app.leaderboard.domain.repository.LeaderboardRepository
import org.hyperskill.app.leaderboard.remote.LeaderboardRemoteDataSourceImpl

internal class LeaderboardDataComponentImpl(appGraph: AppGraph) : LeaderboardDataComponent {
    private val leaderboardRemoteDataSource: LeaderboardRemoteDataSource =
        LeaderboardRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    override val leaderboardRepository: LeaderboardRepository
        get() = LeaderboardRepositoryImpl(leaderboardRemoteDataSource)
}