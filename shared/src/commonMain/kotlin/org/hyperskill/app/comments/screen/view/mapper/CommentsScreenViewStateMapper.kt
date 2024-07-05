package org.hyperskill.app.comments.screen.view.mapper

import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature
import org.hyperskill.app.comments.screen.view.model.CommentsScreenViewState
import org.hyperskill.app.core.view.mapper.date.SharedDateFormatter
import org.hyperskill.app.discussions.domain.model.Discussion
import org.hyperskill.app.discussions.domain.model.getRepliesIds
import org.hyperskill.app.reactions.domain.model.ReactionType
import org.hyperskill.app.reactions.domain.model.commentReactions

internal class CommentsScreenViewStateMapper(
    private val commentThreadTitleMapper: CommentThreadTitleMapper,
    private val dateFormatter: SharedDateFormatter
) {
    fun map(state: CommentsScreenFeature.State): CommentsScreenViewState =
        CommentsScreenViewState(
            navigationTitle = getNavigationTitle(state),
            discussions = getDiscussionsViewState(state)
        )

    private fun getNavigationTitle(state: CommentsScreenFeature.State): String =
        commentThreadTitleMapper.getFormattedCommentThreadStatistics(
            thread = state.commentStatistics.thread,
            count = state.commentStatistics.totalCount
        )

    private fun getDiscussionsViewState(
        state: CommentsScreenFeature.State
    ): CommentsScreenViewState.DiscussionsViewState =
        when (state.discussionsState) {
            CommentsScreenFeature.DiscussionsState.Idle ->
                CommentsScreenViewState.DiscussionsViewState.Idle
            CommentsScreenFeature.DiscussionsState.Loading ->
                CommentsScreenViewState.DiscussionsViewState.Loading
            CommentsScreenFeature.DiscussionsState.Error ->
                CommentsScreenViewState.DiscussionsViewState.Error
            is CommentsScreenFeature.DiscussionsState.Content ->
                mapDiscussionsContentState(state.discussionsState)
        }

    private fun mapDiscussionsContentState(
        state: CommentsScreenFeature.DiscussionsState.Content
    ): CommentsScreenViewState.DiscussionsViewState.Content =
        CommentsScreenViewState.DiscussionsViewState.Content(
            discussions = state.discussions.mapNotNull { discussion ->
                mapDiscussion(
                    discussion = discussion,
                    commentsMap = state.commentsMap,
                    loadingDiscussionReplies = state.loadingDiscussionReplies
                )
            },
            hasNextPage = state.discussions.hasNext,
            isLoadingNextPage = state.isLoadingNextPage
        )

    private fun mapDiscussion(
        discussion: Discussion,
        commentsMap: Map<Long, Comment>,
        loadingDiscussionReplies: Set<Long>
    ): CommentsScreenViewState.DiscussionItem? =
        commentsMap[discussion.id]?.let { rootComment ->
            CommentsScreenViewState.DiscussionItem(
                comment = mapComment(rootComment),
                replies = mapReplies(discussion, commentsMap, loadingDiscussionReplies)
            )
        }

    private fun mapComment(
        comment: Comment
    ): CommentsScreenViewState.CommentItem =
        CommentsScreenViewState.CommentItem(
            id = comment.id,
            authorAvatar = comment.user.avatar,
            authorFullName = comment.user.fullName,
            formattedTime = comment.time?.let { dateFormatter.formatTimeDistance(it) },
            text = comment.localizedText,
            reactions = comment.reactions.filter { it.value > 0 && it.reactionType in ReactionType.commentReactions }
        )

    private fun mapReplies(
        discussion: Discussion,
        commentsMap: Map<Long, Comment>,
        loadingDiscussionReplies: Set<Long>
    ): CommentsScreenViewState.DiscussionReplies {
        val repliesIds = discussion.getRepliesIds()
        val loadedReplies = repliesIds.mapNotNull { commentsMap[it] }

        return when {
            repliesIds.isEmpty() -> CommentsScreenViewState.DiscussionReplies.EmptyReplies
            loadingDiscussionReplies.contains(discussion.id) -> CommentsScreenViewState.DiscussionReplies.LoadingReplies
            loadedReplies.size != repliesIds.size -> CommentsScreenViewState.DiscussionReplies.ShowRepliesButton
            else -> CommentsScreenViewState.DiscussionReplies.Content(loadedReplies.map(::mapComment))
        }
    }
}