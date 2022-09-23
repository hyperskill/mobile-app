package org.hyperskill.app.streak.domain.interactor

import org.hyperskill.app.streak.domain.model.Streak
import org.hyperskill.app.streak.domain.repository.StreakRepository

class StreakInteractor(
    private val streakRepository: StreakRepository
) {
    suspend fun getStreaks(userId: Long): Result<List<Streak>> =
        streakRepository.getStreaks(userId)
}