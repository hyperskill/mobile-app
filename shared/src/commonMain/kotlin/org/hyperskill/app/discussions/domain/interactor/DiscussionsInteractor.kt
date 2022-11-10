package org.hyperskill.app.discussions.domain.interactor

import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.domain.repository.CommentsRepository
import org.hyperskill.app.discussions.domain.model.Discussion
import org.hyperskill.app.discussions.domain.model.DiscussionCommentsTreeItem
import org.hyperskill.app.discussions.domain.repository.DiscussionsRepository
import org.hyperskill.app.discussions.remote.model.DiscussionsRequest
import org.hyperskill.app.discussions.remote.model.DiscussionsResponse

class DiscussionsInteractor(
    private val discussionsRepository: DiscussionsRepository,
    private val commentsRepository: CommentsRepository
) {
    /**
     * Executes a GET discussions request, with the specified **DiscussionsRequest**.
     *
     * @param request Uses for further configuring the HTTP request.
     * @return On success returns a value of [DiscussionsResponse] or a failure with an arbitrary Throwable exception.
     * @see Discussion
     */
    suspend fun getDiscussions(request: DiscussionsRequest): Result<DiscussionsResponse> =
        discussionsRepository.getDiscussions(request)

    /**
     * Returns hints discussions for the specified step by id.
     *
     * @param stepId Id of the step to get hints for.
     * @param page Current pagination page.
     * @return On success returns a value of [DiscussionsResponse] with step hints discussions
     * or a failure with an arbitrary Throwable exception.
     * @see Discussion
     */
    suspend fun getStepHintDiscussions(
        stepId: Long,
        order: DiscussionsRequest.OrderType = DiscussionsRequest.OrderType.MOST_POPULAR,
        page: Int = 1
    ): Result<DiscussionsResponse> =
        discussionsRepository.getStepHintDiscussions(stepId, order, page)

    /**
     * Loads all comments for the specified discussions and assembles tree structure.
     *
     * @param discussions List of the discussions to fetch comments.
     * @return On success returns a list with item value of [DiscussionCommentsTreeItem]
     * or a failure with an arbitrary Throwable exception.
     * @see Discussion
     * @see Comment
     * @see DiscussionCommentsTreeItem
     */
    suspend fun getDiscussionsCommentsTree(discussions: List<Discussion>): Result<List<DiscussionCommentsTreeItem>> =
        kotlin.runCatching {
            val commentsIds = discussions
                .map { it.commentsIds }
                .flatten()
                .distinct()

            if (commentsIds.isEmpty()) {
                return Result.success(emptyList())
            }

            val comments = commentsRepository
                .getComments(commentsIds)
                .getOrThrow()
            val commentsMap = comments.associateBy { it.id }

            val resultTree: MutableList<DiscussionCommentsTreeItem> = mutableListOf()

            for (discussion in discussions) {
                val discussionComment = commentsMap[discussion.id] ?: continue
                val discussionReplies = discussion.commentsIds
                    .filter { it != discussion.id }
                    .mapNotNull { commentsMap[it] }

                val treeItem = DiscussionCommentsTreeItem(
                    discussion,
                    discussionComment,
                    discussionReplies
                )

                resultTree.add(treeItem)
            }

            return Result.success(resultTree)
        }
}