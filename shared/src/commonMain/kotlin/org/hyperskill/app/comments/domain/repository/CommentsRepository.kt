package org.hyperskill.app.comments.domain.repository

import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.domain.model.ReactionType

interface CommentsRepository {
    suspend fun getCommentDetails(commentID: Long): Result<Comment>
    suspend fun abuseComment(commentID: Long)
    suspend fun createReaction(commentID: Long, reaction: ReactionType)
}