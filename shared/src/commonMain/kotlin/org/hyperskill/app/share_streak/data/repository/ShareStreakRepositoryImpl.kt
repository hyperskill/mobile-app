package org.hyperskill.app.share_streak.data.repository

import org.hyperskill.app.share_streak.data.source.ShareStreakCacheDataSource
import org.hyperskill.app.share_streak.domain.repository.ShareStreakRepository

internal class ShareStreakRepositoryImpl(
    private val shareStreakCacheDataSource: ShareStreakCacheDataSource
) : ShareStreakRepository {
    override fun getLastTimeModalShown(): Long? =
        shareStreakCacheDataSource.getLastTimeModalShown()

    override fun setLastTimeModalShown(timestamp: Long) {
        shareStreakCacheDataSource.setLastTimeModalShown(timestamp)
    }
}