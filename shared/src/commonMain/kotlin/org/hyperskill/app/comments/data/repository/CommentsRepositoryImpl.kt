package org.hyperskill.app.comments.data.repository

import org.hyperskill.app.comments.data.source.CommentsRemoteDataSource
import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.domain.repository.CommentsRepository

class CommentsRepositoryImpl(
    private val commentsRemoteDataSource: CommentsRemoteDataSource
) : CommentsRepository {
    override suspend fun getCommentDetails(commentID: Long): Result<Comment> =
        commentsRemoteDataSource.getCommentDetails(commentID)

    override suspend fun abuseComment(commentID: Long) {
        commentsRemoteDataSource.createLike(subject = "abuse", targetType = "comment", targetID = commentID, value = 1)
    }
}