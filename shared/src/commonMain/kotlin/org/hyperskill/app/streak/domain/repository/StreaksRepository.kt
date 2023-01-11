package org.hyperskill.app.streak.domain.repository

import org.hyperskill.app.streak.domain.model.Streak

interface StreaksRepository {
    suspend fun getStreaks(userId: Long): Result<List<Streak>>
}