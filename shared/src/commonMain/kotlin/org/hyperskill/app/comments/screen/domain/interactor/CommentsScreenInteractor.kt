package org.hyperskill.app.comments.screen.domain.interactor

import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.domain.repository.CommentsRepository
import org.hyperskill.app.discussions.domain.repository.DiscussionsRepository
import org.hyperskill.app.discussions.remote.model.DiscussionsResponse

internal class CommentsScreenInteractor(
    private val discussionsRepository: DiscussionsRepository,
    private val commentsRepository: CommentsRepository
) {
    suspend fun getDiscussionsWithRootComments(
        stepId: Long,
        page: Int
    ): Result<Pair<DiscussionsResponse, List<Comment>>> =
        runCatching {
            val discussionsResponse = discussionsRepository
                .getStepCommentThreadDiscussions(stepId = stepId, page = page)
                .getOrThrow()

            val rootCommentsIds = discussionsResponse.discussions.map { it.id }
            val rootComments = commentsRepository
                .getComments(rootCommentsIds)
                .getOrThrow()

            return Result.success(discussionsResponse to rootComments)
        }
}