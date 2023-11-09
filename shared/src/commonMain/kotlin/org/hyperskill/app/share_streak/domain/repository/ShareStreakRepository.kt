package org.hyperskill.app.share_streak.domain.repository

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

interface ShareStreakRepository {
    fun getLastTimeShareStreakShown(): Instant?
    fun setLastTimeShareStreakShown(instant: Instant = Clock.System.now())
    fun clearCache()
}