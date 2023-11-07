package org.hyperskill.app.share_streak.data.repository

import org.hyperskill.app.share_streak.data.source.ShareStreakCacheDataSource
import org.hyperskill.app.share_streak.domain.repository.ShareStreakRepository

internal class ShareStreakRepositoryImpl(
    private val shareStreakCacheDataSource: ShareStreakCacheDataSource
) : ShareStreakRepository {
    override fun getLastTimeShareStreakShownEpochMilliseconds(): Long? =
        shareStreakCacheDataSource.getLastTimeShareStreakShownEpochMilliseconds()

    override fun setLastTimeShareStreakShown(epochMilliseconds: Long) {
        shareStreakCacheDataSource.setLastTimeShareStreakShown(epochMilliseconds)
    }

    override fun clearCache() {
        shareStreakCacheDataSource.clearCache()
    }
}