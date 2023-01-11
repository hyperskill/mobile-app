package org.hyperskill.app.streak.data.repository

import org.hyperskill.app.streak.data.source.StreaksRemoteDataSource
import org.hyperskill.app.streak.domain.model.Streak
import org.hyperskill.app.streak.domain.repository.StreaksRepository

class StreaksRepositoryImpl(
    private val streaksRemoteDataSource: StreaksRemoteDataSource
) : StreaksRepository {
    override suspend fun getStreaks(userId: Long): Result<List<Streak>> =
        streaksRemoteDataSource.getStreaks(userId)
}