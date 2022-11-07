package org.hyperskill.app.comments.data.source

import org.hyperskill.app.comments.domain.model.Comment

interface CommentsRemoteDataSource {
    suspend fun getCommentDetails(commentID: Long): Result<Comment>
    suspend fun createLike(subject: String, targetType: String, targetID: Long, value: Long)
    suspend fun createReaction(commentID: Long, shortName: String)
}