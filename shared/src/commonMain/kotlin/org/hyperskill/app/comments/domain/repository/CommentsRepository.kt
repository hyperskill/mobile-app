package org.hyperskill.app.comments.domain.repository

import org.hyperskill.app.comments.domain.model.Comment

interface CommentsRepository {
    suspend fun getComment(commentId: Long): Result<Comment>
    suspend fun getComments(commentsIds: List<Long>): Result<List<Comment>>
}