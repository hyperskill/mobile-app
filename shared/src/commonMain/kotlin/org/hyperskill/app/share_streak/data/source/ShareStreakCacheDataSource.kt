package org.hyperskill.app.share_streak.data.source

import kotlinx.datetime.Instant

internal interface ShareStreakCacheDataSource {
    fun getLastTimeShareStreakShown(): Instant?
    fun setLastTimeShareStreakShown(instant: Instant)
    fun clearCache()
}