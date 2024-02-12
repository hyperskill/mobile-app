package org.hyperskill.app.request_review.domain.repository

interface RequestReviewRepository {
    fun getLastRequestReviewTimestamp(): Long?
    fun setLastRequestReviewTimestamp(timestamp: Long)

    fun getRequestReviewCount(): Int
    fun incrementRequestReviewCount()
}