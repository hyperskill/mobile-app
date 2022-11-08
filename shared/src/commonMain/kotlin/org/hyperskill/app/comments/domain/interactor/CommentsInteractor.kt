package org.hyperskill.app.comments.domain.interactor

import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.domain.repository.CommentsRepository

class CommentsInteractor(
    private val commentsRepository: CommentsRepository
) {
    suspend fun getComment(commentId: Long): Result<Comment> =
        commentsRepository.getComment(commentId)

    suspend fun getComments(commentsIds: List<Long>): Result<List<Comment>> =
        commentsRepository.getComments(commentsIds)
}