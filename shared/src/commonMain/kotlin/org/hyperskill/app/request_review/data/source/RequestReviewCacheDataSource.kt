package org.hyperskill.app.request_review.data.source

interface RequestReviewCacheDataSource {
    fun getLastRequestReviewTimestamp(): Long?
    fun setLastRequestReviewTimestamp(timestamp: Long)

    fun getRequestReviewCount(): Int
    fun incrementRequestReviewCount()
}