package org.hyperskill.app.android.comments.ui

import org.hyperskill.app.comments.screen.view.model.CommentsScreenViewState
import org.hyperskill.app.comments.screen.view.model.CommentsScreenViewState.DiscussionsViewState
import org.hyperskill.app.reactions.domain.model.ReactionType

object CommentPreviewDataProvider {

    private const val MIXED_DISCUSSIONS_COUNT = 4

    val loadingNextPageDiscussions: DiscussionsViewState.Content =
        DiscussionsViewState.Content(
            discussions = List(2) { i ->
                CommentsScreenViewState.DiscussionItem(
                    comment = getSingleComment(id = i.toLong()),
                    replies = CommentsScreenViewState.DiscussionReplies.EmptyReplies
                )
            },
            hasNextPage = false,
            isLoadingNextPage = true
        )

    val loadingNextPageAndRepliesDiscussions: DiscussionsViewState.Content =
        DiscussionsViewState.Content(
            discussions = List(2) { i ->
                CommentsScreenViewState.DiscussionItem(
                    comment = getSingleComment(id = i.toLong()),
                    replies = if (i == 0) {
                        CommentsScreenViewState.DiscussionReplies.EmptyReplies
                    } else {
                        CommentsScreenViewState.DiscussionReplies.LoadingReplies
                    }
                )
            },
            hasNextPage = false,
            isLoadingNextPage = true
        )

    val mixedDiscussions: DiscussionsViewState.Content =
        DiscussionsViewState.Content(
            discussions = List(MIXED_DISCUSSIONS_COUNT) { rootCommentId ->
                val comment = getSingleComment(id = rootCommentId.toLong())
                CommentsScreenViewState.DiscussionItem(
                    comment = comment,
                    replies = when (rootCommentId) {
                        0 -> CommentsScreenViewState.DiscussionReplies.ShowRepliesButton
                        1 -> CommentsScreenViewState.DiscussionReplies.Content(
                            List(3) { replyCommentId ->
                                getSingleComment(id = replyCommentId.toLong() + MIXED_DISCUSSIONS_COUNT)
                            }
                        )
                        2 -> CommentsScreenViewState.DiscussionReplies.EmptyReplies
                        else -> CommentsScreenViewState.DiscussionReplies.LoadingReplies
                    }
                )
            },
            hasNextPage = false,
            isLoadingNextPage = false
        )

    /* ktlint-disable */
    @Suppress("MaxLineLength")
    fun getSingleComment(id: Long = 0): CommentsScreenViewState.CommentItem =
        CommentsScreenViewState.CommentItem(
            id = id,
            authorAvatar = "",
            authorFullName = "mantraolympics",
            formattedTime = "a month ago",
            text = "Which version of python are you using? In python 3, the type function returns the <class data_type> format.",
            reactions = listOf(
                ReactionType.SMILE,
                ReactionType.PLUS,
                ReactionType.MINUS,
                ReactionType.PLUS,
                ReactionType.MINUS,
                ReactionType.CONFUSED,
                ReactionType.THINKING,
                ReactionType.FIRE,
                ReactionType.CLAP
            ).mapIndexed { index, reactionType ->
                org.hyperskill.app.comments.domain.model.CommentReaction(
                    reactionType = reactionType,
                    value = 1,
                    isSet = index % 2 == 0
                )
            }
        )
}