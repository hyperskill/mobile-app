package org.hyperskill.app.share_streak.domain.repository

interface ShareStreakRepository {
    fun getLastTimeShareStreakShownEpochMilliseconds(): Long?
    fun setLastTimeShareStreakShown(epochMilliseconds: Long)
    fun clearCache()
}