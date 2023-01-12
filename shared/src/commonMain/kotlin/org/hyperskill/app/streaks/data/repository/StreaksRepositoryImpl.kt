package org.hyperskill.app.streaks.data.repository

import org.hyperskill.app.streaks.data.source.StreaksRemoteDataSource
import org.hyperskill.app.streaks.domain.model.Streak
import org.hyperskill.app.streaks.domain.repository.StreaksRepository

class StreaksRepositoryImpl(
    private val streaksRemoteDataSource: StreaksRemoteDataSource
) : StreaksRepository {
    override suspend fun getStreaks(userId: Long): Result<List<Streak>> =
        streaksRemoteDataSource.getStreaks(userId)
}