package org.hyperskill.app.streaks.data.flow

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.hyperskill.app.streaks.domain.flow.StreakFlow
import org.hyperskill.app.streaks.domain.model.Streak

class StreakFlowImpl : StreakFlow {

    private val streakMutableSharedFlow: MutableSharedFlow<Streak?> = MutableSharedFlow()

    override fun observeStreak(): SharedFlow<Streak?> =
        streakMutableSharedFlow

    override suspend fun notifyStreakChanged(streak: Streak?) {
        streakMutableSharedFlow.emit(streak)
    }
}