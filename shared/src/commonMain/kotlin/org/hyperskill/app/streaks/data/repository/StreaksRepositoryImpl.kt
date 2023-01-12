package org.hyperskill.app.streaks.data.repository

import org.hyperskill.app.streaks.data.source.StreaksRemoteDataSource
import org.hyperskill.app.streaks.domain.repository.StreaksRepository
import org.hyperskill.app.streaks.remote.model.StreaksResponse

class StreaksRepositoryImpl(
    private val streaksRemoteDataSource: StreaksRemoteDataSource
) : StreaksRepository {
    override suspend fun getUserStreaks(userId: Long, page: Int): Result<StreaksResponse> =
        streaksRemoteDataSource.getUserStreaks(userId, page)
}