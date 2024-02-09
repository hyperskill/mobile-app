package org.hyperskill.app.request_review.data.repository

import org.hyperskill.app.request_review.data.source.RequestReviewCacheDataSource
import org.hyperskill.app.request_review.domain.repository.RequestReviewRepository

internal class RequestReviewRepositoryImpl(
    private val requestReviewCacheDataSource: RequestReviewCacheDataSource
) : RequestReviewRepository {
    override fun getLastRequestReviewTimestamp(): Long? =
        requestReviewCacheDataSource.getLastRequestReviewTimestamp()

    override fun setLastRequestReviewTimestamp(timestamp: Long) {
        requestReviewCacheDataSource.setLastRequestReviewTimestamp(timestamp)
    }

    override fun getRequestReviewCount(): Int =
        requestReviewCacheDataSource.getRequestReviewCount()

    override fun incrementRequestReviewCount() {
        requestReviewCacheDataSource.incrementRequestReviewCount()
    }
}