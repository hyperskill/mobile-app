package org.hyperskill.app.share_streak.data.source

internal interface ShareStreakCacheDataSource {
    fun getLastTimeShareStreakShownEpochMilliseconds(): Long?
    fun setLastTimeShareStreakShown(epochMilliseconds: Long)
    fun clearCache()
}