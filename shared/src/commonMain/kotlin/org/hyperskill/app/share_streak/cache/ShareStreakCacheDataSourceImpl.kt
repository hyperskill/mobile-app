package org.hyperskill.app.share_streak.cache

import com.russhwolf.settings.Settings
import org.hyperskill.app.share_streak.data.source.ShareStreakCacheDataSource

internal class ShareStreakCacheDataSourceImpl(private val settings: Settings) : ShareStreakCacheDataSource {
    override fun getLastTimeShareStreakShownEpochMilliseconds(): Long? =
        settings.getLongOrNull(ShareStreakCacheKeyValues.SHARE_STREAK_LAST_TIME_SHOWN)

    override fun setLastTimeShareStreakShown(epochMilliseconds: Long) {
        settings.putLong(ShareStreakCacheKeyValues.SHARE_STREAK_LAST_TIME_SHOWN, epochMilliseconds)
    }

    override fun clearCache() {
        settings.remove(ShareStreakCacheKeyValues.SHARE_STREAK_LAST_TIME_SHOWN)
    }
}