package org.hyperskill.commets.screen

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.ResourceProviderStub
import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.domain.model.CommentReaction
import org.hyperskill.app.comments.domain.model.CommentStatisticsEntry
import org.hyperskill.app.comments.domain.model.CommentThread
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature
import org.hyperskill.app.comments.screen.view.mapper.CommentThreadTitleMapper
import org.hyperskill.app.comments.screen.view.mapper.CommentsScreenViewStateMapper
import org.hyperskill.app.comments.screen.view.model.CommentsScreenViewState
import org.hyperskill.app.core.view.mapper.date.SharedDateFormatter
import org.hyperskill.app.discussions.domain.model.Discussion
import org.hyperskill.app.reactions.domain.model.ReactionType
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.commets.domain.model.stub
import ru.nobird.app.core.model.PagedList

class CommentsScreenViewStateMapperTest {
    private val resourceProviderStub = ResourceProviderStub()
    private val dateFormatter = SharedDateFormatter(resourceProviderStub)
    private val commentThreadTitleMapper = CommentThreadTitleMapper(resourceProviderStub)
    private val viewStateMapper = CommentsScreenViewStateMapper(commentThreadTitleMapper, dateFormatter)

    @Test
    fun `Idle state should be mapped to Idle view state`() {
        val state = stubState(discussionsState = CommentsScreenFeature.DiscussionsState.Idle)
        val viewState = viewStateMapper.map(state)
        assertEquals(CommentsScreenViewState.DiscussionsViewState.Idle, viewState.discussions)
    }

    @Test
    fun `Loading state should be mapped to Loading view state`() {
        val state = stubState(discussionsState = CommentsScreenFeature.DiscussionsState.Loading)
        val viewState = viewStateMapper.map(state)
        assertEquals(CommentsScreenViewState.DiscussionsViewState.Loading, viewState.discussions)
    }

    @Test
    fun `Error state should be mapped to Error view state`() {
        val state = stubState(discussionsState = CommentsScreenFeature.DiscussionsState.Error)
        val viewState = viewStateMapper.map(state)
        assertEquals(CommentsScreenViewState.DiscussionsViewState.Error, viewState.discussions)
    }

    @Test
    fun `Content state should be mapped to Content view state`() {
        val comment = Comment.stub(
            id = 1,
            reactions = listOf(CommentReaction(reactionType = ReactionType.SMILE, value = 1, isSet = false))
        )
        val discussion = Discussion(id = 1, commentsIds = listOf(comment.id))
        val discussionsState = CommentsScreenFeature.DiscussionsState.Content(
            discussions = PagedList(
                list = listOf(discussion),
                page = 1,
                hasNext = false,
                hasPrev = false
            ),
            commentsMap = mapOf(discussion.id to comment),
            loadingDiscussionReplies = emptySet(),
            isLoadingNextPage = false
        )
        val state = stubState(discussionsState = discussionsState)

        val viewState = viewStateMapper.map(state)
        val expectedViewState = CommentsScreenViewState(
            navigationTitle = commentThreadTitleMapper.getFormattedCommentThreadStatistics(
                thread = state.commentStatistics.thread,
                count = state.commentStatistics.totalCount
            ),
            discussions = CommentsScreenViewState.DiscussionsViewState.Content(
                discussions = listOf(
                    CommentsScreenViewState.DiscussionItem(
                        comment = CommentsScreenViewState.CommentItem(
                            id = comment.id,
                            authorAvatar = comment.user.avatar,
                            authorFullName = comment.user.fullName,
                            formattedTime = null,
                            text = comment.text,
                            reactions = comment.reactions
                        ),
                        replies = CommentsScreenViewState.DiscussionReplies.EmptyReplies
                    )
                ),
                hasNextPage = false,
                isLoadingNextPage = false
            )
        )
        assertEquals(expectedViewState, viewState)
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
}