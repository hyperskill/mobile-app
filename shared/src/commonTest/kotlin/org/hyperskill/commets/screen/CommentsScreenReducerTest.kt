package org.hyperskill.commets.screen

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.domain.model.CommentReaction
import org.hyperskill.app.comments.domain.model.CommentStatisticsEntry
import org.hyperskill.app.comments.domain.model.CommentThread
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature
import org.hyperskill.app.comments.screen.presentation.CommentsScreenReducer
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.discussions.domain.model.Discussion
import org.hyperskill.app.discussions.remote.model.DiscussionsResponse
import org.hyperskill.app.discussions.remote.model.toPagedList
import org.hyperskill.app.reactions.domain.model.ReactionType
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.commets.domain.model.stub
import ru.nobird.app.core.model.PagedList

class CommentsScreenReducerTest {
    private val reducer = CommentsScreenReducer()

    @Test
    fun `Initialize message should trigger discussions fetching`() {
        val initialState = stubState(discussionsState = CommentsScreenFeature.DiscussionsState.Idle)
        val (state, actions) = reducer.reduce(initialState, CommentsScreenFeature.Message.Initialize)
        assertTrue(
            actions.contains(
                CommentsScreenFeature.InternalAction.FetchDiscussions(
                    initialState.stepRoute.stepId,
                    1
                )
            )
        )
        assertEquals(CommentsScreenFeature.DiscussionsState.Loading, state.discussionsState)
    }

    @Test
    fun `RetryContentLoading message should trigger discussions fetching when in Error state`() {
        val initialState = stubState(discussionsState = CommentsScreenFeature.DiscussionsState.Error)
        val (state, actions) = reducer.reduce(initialState, CommentsScreenFeature.Message.RetryContentLoading)
        assertTrue(
            actions.contains(
                CommentsScreenFeature.InternalAction.FetchDiscussions(
                    initialState.stepRoute.stepId,
                    1
                )
            )
        )
        assertEquals(CommentsScreenFeature.DiscussionsState.Loading, state.discussionsState)
    }

    @Test
    fun `DiscussionsFetchError message should set state to Error when in Loading state`() {
        val initialState = stubState(discussionsState = CommentsScreenFeature.DiscussionsState.Loading)
        val (state, _) = reducer.reduce(initialState, CommentsScreenFeature.InternalMessage.DiscussionsFetchError)
        assertEquals(CommentsScreenFeature.DiscussionsState.Error, state.discussionsState)
    }

    @Test
    fun `DiscussionsFetchSuccess message should update state and discussions when in Loading state`() {
        val initialState = stubState(discussionsState = CommentsScreenFeature.DiscussionsState.Loading)
        val discussionsResponse = DiscussionsResponse(
            meta = Meta(
                page = 1,
                hasNext = true,
                hasPrevious = false
            ),
            discussions = listOf(
                Discussion(id = 1, commentsIds = listOf(1, 2))
            )
        )
        val rootComments = listOf(Comment.stub(id = 1), Comment.stub(id = 2))
        val message = CommentsScreenFeature.InternalMessage.DiscussionsFetchSuccess(discussionsResponse, rootComments)

        val (state, _) = reducer.reduce(initialState, message)
        val expectedState = CommentsScreenFeature.DiscussionsState.Content(
            discussions = discussionsResponse.toPagedList(),
            commentsMap = rootComments.associateBy { it.id },
            loadingDiscussionReplies = emptySet(),
            isLoadingNextPage = false
        )

        assertTrue(state.discussionsState is CommentsScreenFeature.DiscussionsState.Content)
        assertTrue(pagedListsEqual(expectedState.discussions, state.discussionsState.discussions))
        assertEquals(expectedState.commentsMap, state.discussionsState.commentsMap)
        assertEquals(expectedState.loadingDiscussionReplies, state.discussionsState.loadingDiscussionReplies)
        assertEquals(expectedState.isLoadingNextPage, state.discussionsState.isLoadingNextPage)
    }

    @Test
    fun `DiscussionsFetchSuccess message should update state and append discussions when in Content state`() {
        val initialDiscussions = PagedList(
            list = listOf(
                Discussion(id = 1, commentsIds = listOf(1, 2))
            ),
            page = 1,
            hasNext = true,
            hasPrev = false
        )
        val initialCommentsMap = mapOf(
            1L to Comment.stub(id = 1),
            2L to Comment.stub(id = 2)
        )
        val initialState = stubState(
            discussionsState = CommentsScreenFeature.DiscussionsState.Content(
                discussions = initialDiscussions,
                commentsMap = initialCommentsMap,
                loadingDiscussionReplies = emptySet(),
                isLoadingNextPage = true
            )
        )
        val discussionsResponse = DiscussionsResponse(
            meta = Meta(
                page = 2,
                hasNext = false,
                hasPrevious = true
            ),
            discussions = listOf(Discussion(id = 3, commentsIds = listOf(3, 4)))
        )
        val rootComments = listOf(Comment.stub(id = 3), Comment.stub(id = 4))
        val message = CommentsScreenFeature.InternalMessage.DiscussionsFetchSuccess(discussionsResponse, rootComments)

        val (state, _) = reducer.reduce(initialState, message)
        val expectedState = CommentsScreenFeature.DiscussionsState.Content(
            discussions = PagedList(
                list = initialDiscussions.toList() + discussionsResponse.discussions,
                page = discussionsResponse.meta.page,
                hasNext = discussionsResponse.meta.hasNext,
                hasPrev = discussionsResponse.meta.hasPrevious
            ),
            commentsMap = initialCommentsMap + rootComments.associateBy { it.id },
            loadingDiscussionReplies = emptySet(),
            isLoadingNextPage = false
        )

        assertTrue(state.discussionsState is CommentsScreenFeature.DiscussionsState.Content)
        assertTrue(pagedListsEqual(expectedState.discussions, state.discussionsState.discussions))
        assertEquals(expectedState.commentsMap, state.discussionsState.commentsMap)
        assertEquals(expectedState.loadingDiscussionReplies, state.discussionsState.loadingDiscussionReplies)
        assertEquals(expectedState.isLoadingNextPage, state.discussionsState.isLoadingNextPage)
    }

    @Test
    fun `ShowMoreDiscussionsClicked message should trigger fetching next page of discussions`() {
        val discussionsState = CommentsScreenFeature.DiscussionsState.Content(
            discussions = PagedList(
                list = listOf(Discussion(id = 1, commentsIds = listOf(1, 2))),
                page = 1,
                hasNext = true,
                hasPrev = false
            ),
            commentsMap = mapOf(),
            loadingDiscussionReplies = emptySet(),
            isLoadingNextPage = false
        )
        val initialState = stubState(discussionsState = discussionsState)

        val (state, actions) = reducer.reduce(initialState, CommentsScreenFeature.Message.ShowMoreDiscussionsClicked)
        assertTrue(
            actions.contains(
                CommentsScreenFeature.InternalAction.FetchDiscussions(
                    initialState.stepRoute.stepId,
                    2
                )
            )
        )
        assertEquals(discussionsState.copy(isLoadingNextPage = true), state.discussionsState)
    }

    @Test
    fun `ReactionClicked message should update comment reaction and trigger CreateCommentReaction action`() {
        val commentId = 1L
        val reactionType = ReactionType.SMILE
        val comment = Comment.stub(
            id = commentId,
            reactions = listOf(CommentReaction(reactionType, 0, false))
        )
        val discussionsState = CommentsScreenFeature.DiscussionsState.Content(
            discussions = PagedList(
                list = listOf(Discussion(id = 1, commentsIds = listOf(commentId))),
                page = 1,
                hasNext = false,
                hasPrev = false
            ),
            commentsMap = mapOf(commentId to comment),
            loadingDiscussionReplies = emptySet(),
            isLoadingNextPage = false
        )
        val initialState = stubState(discussionsState = discussionsState)

        val message = CommentsScreenFeature.Message.ReactionClicked(commentId, reactionType)
        val (state, actions) = reducer.reduce(initialState, message)

        val updatedComment = comment.copy(
            reactions = listOf(CommentReaction(reactionType, 1, true))
        )
        val expectedCommentsMap = mapOf(commentId to updatedComment)

        assertEquals(discussionsState.copy(commentsMap = expectedCommentsMap), state.discussionsState)
        assertTrue(
            actions.contains(
                CommentsScreenFeature.InternalAction.CreateCommentReaction(
                    commentId,
                    reactionType
                )
            )
        )
    }

    @Test
    fun `ReactionClicked message should update comment reaction and trigger RemoveCommentReaction action`() {
        val commentId = 1L
        val reactionType = ReactionType.SMILE
        val comment = Comment.stub(
            id = commentId,
            reactions = listOf(CommentReaction(reactionType, 1, true))
        )
        val discussionsState = CommentsScreenFeature.DiscussionsState.Content(
            discussions = PagedList(
                list = listOf(Discussion(id = 1, commentsIds = listOf(commentId))),
                page = 1,
                hasNext = false,
                hasPrev = false
            ),
            commentsMap = mapOf(commentId to comment),
            loadingDiscussionReplies = emptySet(),
            isLoadingNextPage = false
        )
        val initialState = stubState(discussionsState = discussionsState)

        val message = CommentsScreenFeature.Message.ReactionClicked(commentId, reactionType)
        val (state, actions) = reducer.reduce(initialState, message)

        val updatedComment = comment.copy(
            reactions = listOf(CommentReaction(reactionType, 0, false))
        )
        val expectedCommentsMap = mapOf(commentId to updatedComment)

        assertEquals(discussionsState.copy(commentsMap = expectedCommentsMap), state.discussionsState)
        assertTrue(
            actions.contains(
                CommentsScreenFeature.InternalAction.RemoveCommentReaction(
                    commentId,
                    reactionType
                )
            )
        )
    }

    private fun stubState(
        stepRoute: StepRoute = StepRoute.Learn.Step(1, null),
        commentStatistics: CommentStatisticsEntry = CommentStatisticsEntry(
            thread = CommentThread.COMMENT,
            totalCount = 1
        ),
        discussionsState: CommentsScreenFeature.DiscussionsState
    ): CommentsScreenFeature.State =
        CommentsScreenFeature.State(
            stepRoute = stepRoute,
            commentStatistics = commentStatistics,
            discussionsState = discussionsState
        )

    private fun pagedListsEqual(lhs: PagedList<Discussion>, rhs: PagedList<Discussion>): Boolean =
        lhs.page == rhs.page && lhs.hasNext == rhs.hasNext && lhs.hasPrev == rhs.hasPrev && lhs.toList() == rhs.toList()
}