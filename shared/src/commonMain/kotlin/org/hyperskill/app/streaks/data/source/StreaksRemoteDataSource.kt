package org.hyperskill.app.streaks.data.source

import org.hyperskill.app.streaks.remote.model.StreaksResponse

interface StreaksRemoteDataSource {
    suspend fun getUserStreaks(userId: Long, page: Int): Result<StreaksResponse>

    suspend fun recoverStreak(): Result<StreaksResponse>

    suspend fun cancelStreakRecovery(): Result<StreaksResponse>
}