package org.hyperskill.app.streak.domain.interactor

import org.hyperskill.app.streak.domain.model.Streak
import org.hyperskill.app.streak.domain.repository.StreaksRepository

class StreaksInteractor(
    private val streaksRepository: StreaksRepository
) {
    suspend fun getStreaks(userId: Long): Result<List<Streak>> =
        streaksRepository.getStreaks(userId)
}