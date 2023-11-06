package org.hyperskill.app.share_streak.cache

import com.russhwolf.settings.Settings
import org.hyperskill.app.share_streak.data.source.ShareStreakCacheDataSource

internal class ShareStreakCacheDataSourceImpl(private val settings: Settings) : ShareStreakCacheDataSource {
    override fun getLastTimeModalShown(): Long? =
        settings.getLongOrNull(ShareStreakCacheKeyValues.SHARE_STREAK_LAST_TIME_MODAL_SHOWN)

    override fun setLastTimeModalShown(timestamp: Long) {
        settings.putLong(ShareStreakCacheKeyValues.SHARE_STREAK_LAST_TIME_MODAL_SHOWN, timestamp)
    }
}