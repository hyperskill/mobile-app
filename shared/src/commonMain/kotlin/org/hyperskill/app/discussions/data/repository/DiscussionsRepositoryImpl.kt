package org.hyperskill.app.discussions.data.repository

import org.hyperskill.app.discussions.data.source.DiscussionsRemoteDataSource
import org.hyperskill.app.discussions.domain.repository.DiscussionsRepository
import org.hyperskill.app.discussions.remote.model.DiscussionsRequest
import org.hyperskill.app.discussions.remote.model.DiscussionsResponse

class DiscussionsRepositoryImpl(
    private val discussionsRemoteDataSource: DiscussionsRemoteDataSource
) : DiscussionsRepository {
    override suspend fun getDiscussions(request: DiscussionsRequest): Result<DiscussionsResponse> =
        discussionsRemoteDataSource.getDiscussions(request)

    override suspend fun getStepHints(stepId: Long, page: Int): Result<DiscussionsResponse> =
        discussionsRemoteDataSource
            .getDiscussions(
                DiscussionsRequest(
                    target = DiscussionsRequest.TargetType.STEP,
                    targetId = stepId,
                    thread = DiscussionsRequest.ThreadType.HINT,
                    order = DiscussionsRequest.OrderType.MOST_POPULAR,
                    isSpam = false,
                    page = page
                )
            )
}