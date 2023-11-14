package org.hyperskill.app.gamification_toolbar.data.repository

import org.hyperskill.app.core.data.repository.BaseStateRepository
import org.hyperskill.app.gamification_toolbar.data.source.GamificationToolbarRemoteDataSource
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarData
import org.hyperskill.app.gamification_toolbar.domain.repository.CurrentGamificationToolbarDataStateRepository

class CurrentGamificationToolbarDataStateRepositoryImpl(
    private val gamificationToolbarRemoteDataSource: GamificationToolbarRemoteDataSource
) : CurrentGamificationToolbarDataStateRepository, BaseStateRepository<GamificationToolbarData>() {
    override suspend fun loadState(): Result<GamificationToolbarData> =
        gamificationToolbarRemoteDataSource.getGamificationToolbarData()
}