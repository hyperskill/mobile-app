package org.hyperskill.app.share_streak.data.source

internal interface ShareStreakCacheDataSource {
    fun getLastTimeModalShown(): Long?
    fun setLastTimeModalShown(timestamp: Long)
}