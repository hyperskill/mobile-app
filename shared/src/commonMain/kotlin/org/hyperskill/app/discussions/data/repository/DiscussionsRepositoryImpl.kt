package org.hyperskill.app.discussions.data.repository

import org.hyperskill.app.discussions.data.source.DiscussionsRemoteDataSource
import org.hyperskill.app.discussions.domain.repository.DiscussionsRepository
import org.hyperskill.app.discussions.remote.model.DiscussionsRequest
import org.hyperskill.app.discussions.remote.model.DiscussionsResponse

internal class DiscussionsRepositoryImpl(
    private val discussionsRemoteDataSource: DiscussionsRemoteDataSource
) : DiscussionsRepository {
    override suspend fun getDiscussions(request: DiscussionsRequest): Result<DiscussionsResponse> =
        discussionsRemoteDataSource.getDiscussions(request)

    override suspend fun getStepHintThreadDiscussions(
        stepId: Long,
        order: DiscussionsRequest.OrderType,
        page: Int
    ): Result<DiscussionsResponse> =
        discussionsRemoteDataSource
            .getDiscussions(
                DiscussionsRequest(
                    target = DiscussionsRequest.TargetType.STEP,
                    targetId = stepId,
                    thread = DiscussionsRequest.ThreadType.HINT,
                    order = order,
                    isSpam = false,
                    page = page
                )
            )

    override suspend fun getStepCommentThreadDiscussions(
        stepId: Long,
        page: Int
    ): Result<DiscussionsResponse> =
        discussionsRemoteDataSource
            .getDiscussions(
                DiscussionsRequest(
                    target = DiscussionsRequest.TargetType.STEP,
                    targetId = stepId,
                    thread = DiscussionsRequest.ThreadType.COMMENT,
                    order = DiscussionsRequest.OrderType.BEST_RATED,
                    page = page
                )
            )
}