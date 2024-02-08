package org.hyperskill.app.request_review.cache

import com.russhwolf.settings.Settings
import org.hyperskill.app.request_review.data.source.RequestReviewCacheDataSource

internal class RequestReviewCacheDataSourceImpl(
    private val settings: Settings
) : RequestReviewCacheDataSource {
    override fun getLastRequestReviewTimestamp(): Long? =
        settings.getLongOrNull(RequestReviewCacheKeyValues.LAST_REQUEST_REVIEW_TIMESTAMP)

    override fun setLastRequestReviewTimestamp(timestamp: Long) {
        settings.putLong(RequestReviewCacheKeyValues.LAST_REQUEST_REVIEW_TIMESTAMP, timestamp)
    }

    override fun getRequestReviewCount(): Int =
        settings.getInt(RequestReviewCacheKeyValues.REQUEST_REVIEW_COUNT, 0)

    override fun incrementRequestReviewCount() {
        settings.putInt(RequestReviewCacheKeyValues.REQUEST_REVIEW_COUNT, getRequestReviewCount() + 1)
    }
}