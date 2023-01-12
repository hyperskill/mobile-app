package org.hyperskill.app.streaks.domain.interactor

import org.hyperskill.app.streaks.domain.model.Streak
import org.hyperskill.app.streaks.domain.repository.StreaksRepository

class StreaksInteractor(
    private val streaksRepository: StreaksRepository
) {
    suspend fun getStreaks(userId: Long): Result<List<Streak>> =
        streaksRepository.getStreaks(userId)
}