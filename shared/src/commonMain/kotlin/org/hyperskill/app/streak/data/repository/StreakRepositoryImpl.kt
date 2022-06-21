package org.hyperskill.app.streak.data.repository

import org.hyperskill.app.streak.data.source.StreakRemoteDataSource
import org.hyperskill.app.streak.domain.model.Streak
import org.hyperskill.app.streak.domain.repository.StreakRepository

class StreakRepositoryImpl(
    private val streakRemoteDataSource: StreakRemoteDataSource
) : StreakRepository {
    override suspend fun getStreaks(userId: Long): Result<List<Streak>> =
        streakRemoteDataSource.getStreaks(userId)
}