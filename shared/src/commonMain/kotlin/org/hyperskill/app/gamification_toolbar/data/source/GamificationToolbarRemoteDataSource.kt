package org.hyperskill.app.gamification_toolbar.data.source

import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarData

interface GamificationToolbarRemoteDataSource {
    suspend fun getGamificationToolbarData(): Result<GamificationToolbarData>
}