package org.hyperskill.app.streaks.domain.flow

import kotlinx.coroutines.flow.SharedFlow
import org.hyperskill.app.streaks.domain.model.Streak

interface StreakFlow {
    fun observeStreak(): SharedFlow<Streak?>

    suspend fun notifyStreakChanged(streak: Streak?)
}