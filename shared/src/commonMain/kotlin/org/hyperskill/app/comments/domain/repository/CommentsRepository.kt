package org.hyperskill.app.comments.domain.repository

import org.hyperskill.app.comments.domain.model.Comment

interface CommentsRepository {
    suspend fun getCommentDetails(commentID: Long): Result<Comment>
}