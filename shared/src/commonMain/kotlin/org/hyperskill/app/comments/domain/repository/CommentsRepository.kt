package org.hyperskill.app.comments.domain.repository

import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.domain.model.Like
import org.hyperskill.app.comments.domain.model.Reaction
import org.hyperskill.app.comments.domain.model.ReactionType

interface CommentsRepository {
    suspend fun getHintsIDs(stepId: Long): List<Long>
    suspend fun getCommentDetails(commentId: Long): Result<Comment>
    suspend fun abuseComment(commentId: Long): Result<Like>
    suspend fun createReaction(commentId: Long, reaction: ReactionType): Result<Reaction>
}