package org.hyperskill.app.share_streak.data.repository

import kotlinx.datetime.Instant
import org.hyperskill.app.share_streak.data.source.ShareStreakCacheDataSource
import org.hyperskill.app.share_streak.domain.repository.ShareStreakRepository

internal class ShareStreakRepositoryImpl(
    private val shareStreakCacheDataSource: ShareStreakCacheDataSource
) : ShareStreakRepository {
    override fun getLastTimeShareStreakShown(): Instant? =
        shareStreakCacheDataSource.getLastTimeShareStreakShown()

    override fun setLastTimeShareStreakShown(instant: Instant) {
        shareStreakCacheDataSource.setLastTimeShareStreakShown(instant)
    }

    override fun clearCache() {
        shareStreakCacheDataSource.clearCache()
    }
}