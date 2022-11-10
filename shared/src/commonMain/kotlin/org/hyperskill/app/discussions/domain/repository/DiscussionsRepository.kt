package org.hyperskill.app.discussions.domain.repository

import org.hyperskill.app.discussions.remote.model.DiscussionsRequest
import org.hyperskill.app.discussions.remote.model.DiscussionsResponse

interface DiscussionsRepository {
    suspend fun getDiscussions(request: DiscussionsRequest): Result<DiscussionsResponse>
    suspend fun getStepHintDiscussions(
        stepId: Long,
        order: DiscussionsRequest.OrderType = DiscussionsRequest.OrderType.MOST_POPULAR,
        page: Int = 1
    ): Result<DiscussionsResponse>
}