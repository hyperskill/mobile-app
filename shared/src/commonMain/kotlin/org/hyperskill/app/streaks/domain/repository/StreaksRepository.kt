package org.hyperskill.app.streaks.domain.repository

import org.hyperskill.app.streaks.domain.model.Streak

interface StreaksRepository {
    suspend fun getStreaks(userId: Long): Result<List<Streak>>
}