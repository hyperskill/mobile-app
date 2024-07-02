package org.hyperskill.app.comments.screen.view.model

import ru.nobird.app.core.model.Identifiable

data class CommentsScreenViewState(
    val navigationTitle: String,
    val discussions: DiscussionsViewState
) {
    sealed interface DiscussionsViewState {
        object Idle : DiscussionsViewState
        object Loading : DiscussionsViewState
        object Error : DiscussionsViewState
        data class Content(
            val discussions: List<DiscussionItem>,
            val hasNextPage: Boolean,
            val isLoadingNextPage: Boolean
        ) : DiscussionsViewState
    }

    data class DiscussionItem(
        val comment: CommentItem,
        val replies: DiscussionReplies
    ) : Identifiable<Long> {
        override val id: Long
            get() = comment.id
    }

    sealed interface DiscussionReplies {
        object EmptyReplies : DiscussionReplies
        object ShowRepliesButton : DiscussionReplies
        object LoadingReplies : DiscussionReplies
        data class Content(val replies: List<CommentItem>) : DiscussionReplies
    }

    data class CommentItem(
        override val id: Long,
        val authorAvatar: String,
        val authorFullName: String,
        val formattedTime: String?,
        val text: String
    ) : Identifiable<Long>
}