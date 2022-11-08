package org.hyperskill.app.comments.domain.repository

import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.remote.model.CommentsResponse

interface CommentsRepository {
    suspend fun getComment(commentId: Long): Result<Comment>
    suspend fun getComments(commentsIds: List<Long>): Result<CommentsResponse>
}