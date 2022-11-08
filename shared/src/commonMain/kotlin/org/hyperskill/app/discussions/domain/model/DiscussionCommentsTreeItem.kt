package org.hyperskill.app.discussions.domain.model

import org.hyperskill.app.comments.domain.model.Comment

/**
 * Represents a discussion thread with root comment and replies.
 *
 * @property discussion Discussion.
 * @property discussionComment Root comment.
 * @property discussionReplies Replies for the root comment.
 * @see Comment
 */
data class DiscussionCommentsTreeItem(
    val discussion: Discussion,
    val discussionComment: Comment,
    val discussionReplies: List<Comment>
)