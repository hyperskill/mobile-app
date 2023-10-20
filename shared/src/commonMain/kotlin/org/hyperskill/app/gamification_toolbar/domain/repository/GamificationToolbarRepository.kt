package org.hyperskill.app.gamification_toolbar.domain.repository

import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarData

interface GamificationToolbarRepository {
    suspend fun getGamificationToolbarData(): Result<GamificationToolbarData>
}