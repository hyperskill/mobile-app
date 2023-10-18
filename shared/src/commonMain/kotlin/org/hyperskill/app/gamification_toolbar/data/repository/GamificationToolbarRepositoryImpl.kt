package org.hyperskill.app.gamification_toolbar.data.repository

import org.hyperskill.app.gamification_toolbar.data.source.GamificationToolbarRemoteDataSource
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarData
import org.hyperskill.app.gamification_toolbar.domain.repository.GamificationToolbarRepository

class GamificationToolbarRepositoryImpl(
    private val gamificationToolbarRemoteDataSource: GamificationToolbarRemoteDataSource
) : GamificationToolbarRepository {
    override suspend fun getGamificationToolbarData(): Result<GamificationToolbarData> =
        gamificationToolbarRemoteDataSource.getGamificationToolbarData()
}