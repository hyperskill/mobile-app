package org.hyperskill.app.comments.data.source

import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.remote.model.CommentsResponse

interface CommentsRemoteDataSource {
    suspend fun getComment(commentId: Long): Result<Comment>
    suspend fun getComments(commentsIds: List<Long>): Result<CommentsResponse>
}