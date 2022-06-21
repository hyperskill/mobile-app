package org.hyperskill.app.streak.domain.interactor

import org.hyperskill.app.streak.domain.repository.StreakRepository

class StreakInteractor(
    private val streakRepository: StreakRepository
) {
    suspend fun getStreaks(userId: Long) =
        streakRepository.getStreaks(userId)
}