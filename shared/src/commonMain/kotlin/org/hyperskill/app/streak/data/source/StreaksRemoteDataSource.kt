package org.hyperskill.app.streak.data.source

import org.hyperskill.app.streak.domain.model.Streak

interface StreaksRemoteDataSource {
    suspend fun getStreaks(userId: Long): Result<List<Streak>>
}