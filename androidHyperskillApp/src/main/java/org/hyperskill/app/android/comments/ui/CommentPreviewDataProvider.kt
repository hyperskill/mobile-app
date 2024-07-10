package org.hyperskill.app.android.comments.ui

import org.hyperskill.app.comments.screen.view.model.CommentsScreenViewState
import org.hyperskill.app.reactions.domain.model.ReactionType

object CommentPreviewDataProvider {

    private const val DISCUSSIONS_COUNT = 4

    fun getDiscussions(): List<CommentsScreenViewState.DiscussionItem> =
        List(DISCUSSIONS_COUNT) { rootCommentId ->
            val comment = getSingleComment(id = rootCommentId.toLong())
            CommentsScreenViewState.DiscussionItem(
                comment = comment,
                replies = when (rootCommentId) {
                    0 -> CommentsScreenViewState.DiscussionReplies.ShowRepliesButton
                    1 -> CommentsScreenViewState.DiscussionReplies.Content(
                        List(3) { replyCommentId ->
                            getSingleComment(id = replyCommentId.toLong() + DISCUSSIONS_COUNT)
                        }
                    )
                    2 -> CommentsScreenViewState.DiscussionReplies.EmptyReplies
                    else -> CommentsScreenViewState.DiscussionReplies.LoadingReplies
                }
            )
        }

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