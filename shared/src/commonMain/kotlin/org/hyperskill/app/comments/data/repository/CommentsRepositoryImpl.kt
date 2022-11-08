package org.hyperskill.app.comments.data.repository

import org.hyperskill.app.comments.data.source.CommentsRemoteDataSource
import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.domain.repository.CommentsRepository
import org.hyperskill.app.comments.remote.model.CommentsResponse

class CommentsRepositoryImpl(
    private val commentsRemoteDataSource: CommentsRemoteDataSource
) : CommentsRepository {
    override suspend fun getComment(commentId: Long): Result<Comment> =
        commentsRemoteDataSource.getComment(commentId)

    override suspend fun getComments(commentsIds: List<Long>): Result<CommentsResponse> =
        commentsRemoteDataSource.getComments(commentsIds)
}