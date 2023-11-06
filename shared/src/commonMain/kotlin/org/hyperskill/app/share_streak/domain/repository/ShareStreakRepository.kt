package org.hyperskill.app.share_streak.domain.repository

interface ShareStreakRepository {
    fun getLastTimeModalShown(): Long?
    fun setLastTimeModalShown(timestamp: Long)
}