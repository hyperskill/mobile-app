package org.hyperskill.streaks.domain.model

import org.hyperskill.app.streaks.domain.model.HistoricalStreak
import org.hyperskill.app.streaks.domain.model.Streak

fun Streak.Companion.stub(
    userId: Long = 0L,
    currentStreak: Int = 0,
    maxStreak: Int = 0,
    isNewRecord: Boolean = false,
    canFreeze: Boolean = false,
    canBuyFreeze: Boolean = false,
    canBeRecovered: Boolean = false,
    recoveryPrice: Int = 0,
    previousStreak: Int = 0,
    history: List<HistoricalStreak> = emptyList()
): Streak =
    Streak(
        userId = userId,
        currentStreak = currentStreak,
        maxStreak = maxStreak,
        isNewRecord = isNewRecord,
        canFreeze = canFreeze,
        canBuyFreeze = canBuyFreeze,
        canBeRecovered = canBeRecovered,
        recoveryPrice = recoveryPrice,
        previousStreak = previousStreak,
        history = history
    )