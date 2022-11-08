package org.hyperskill.app.discussions.domain.model

import org.hyperskill.app.comments.domain.model.Comment

data class DiscussionCommentsTreeItem(
    val discussion: Discussion,
    val discussionComment: Comment,
    val discussionReplies: List<Comment>
)