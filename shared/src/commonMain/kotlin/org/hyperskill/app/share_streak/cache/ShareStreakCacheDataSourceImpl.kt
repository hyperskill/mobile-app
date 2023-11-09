package org.hyperskill.app.share_streak.cache

import com.russhwolf.settings.Settings
import kotlinx.datetime.Instant
import org.hyperskill.app.share_streak.data.source.ShareStreakCacheDataSource

internal class ShareStreakCacheDataSourceImpl(private val settings: Settings) : ShareStreakCacheDataSource {
    override fun getLastTimeShareStreakShown(): Instant? =
        settings
            .getLongOrNull(ShareStreakCacheKeyValues.SHARE_STREAK_LAST_TIME_SHOWN)
            ?.let(Instant.Companion::fromEpochMilliseconds)

    override fun setLastTimeShareStreakShown(instant: Instant) {
        settings.putLong(ShareStreakCacheKeyValues.SHARE_STREAK_LAST_TIME_SHOWN, instant.toEpochMilliseconds())
    }

    override fun clearCache() {
        settings.remove(ShareStreakCacheKeyValues.SHARE_STREAK_LAST_TIME_SHOWN)
    }
}