package org.hyperskill.app.streaks.domain.interactor

import org.hyperskill.app.streaks.domain.model.Streak
import org.hyperskill.app.streaks.domain.repository.StreaksRepository
import org.hyperskill.app.streaks.remote.model.StreaksResponse

class StreaksInteractor(
    private val streaksRepository: StreaksRepository
) {
    suspend fun getUserStreak(userId: Long): Result<Streak?> =
        streaksRepository.getUserStreak(userId)

    suspend fun recoverStreak(): Result<StreaksResponse> =
        streaksRepository.recoverStreak()

    suspend fun cancelStreakRecovery(): Result<StreaksResponse> =
        streaksRepository.recoverStreak()
}