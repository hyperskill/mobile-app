package org.hyperskill.app.streaks.data.source

import org.hyperskill.app.streaks.domain.model.Streak

interface StreaksRemoteDataSource {
    suspend fun getStreaks(userId: Long): Result<List<Streak>>
}