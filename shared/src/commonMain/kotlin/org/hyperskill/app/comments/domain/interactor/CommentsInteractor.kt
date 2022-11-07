package org.hyperskill.app.comments.domain.interactor

import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.domain.repository.CommentsRepository

class CommentsInteractor(
    private val commentsRepository: CommentsRepository
) {
    suspend fun getCommentDetails(commentId: Long): Result<Comment> =
        commentsRepository.getCommentDetails(commentId)
}