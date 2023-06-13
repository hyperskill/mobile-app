package org.hyperskill.app.streaks.domain.repository

import org.hyperskill.app.streaks.domain.model.Streak
import org.hyperskill.app.streaks.remote.model.StreaksResponse

interface StreaksRepository {
    suspend fun getUserStreaks(userId: Long, page: Int = 1): Result<StreaksResponse>

    suspend fun getUserStreak(userId: Long): Result<Streak?> =
        getUserStreaks(userId).map { it.streaks.firstOrNull() }

    suspend fun recoverStreak(): Result<StreaksResponse>

    suspend fun cancelStreakRecovery(): Result<StreaksResponse>
}