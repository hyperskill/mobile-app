package org.hyperskill.app.comments.domain.interactor

import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.domain.repository.CommentsRepository

class CommentsInteractor(
    private val commentsRepository: CommentsRepository
) {
    suspend fun getHintsIDs(stepID: Long): List<Long> =
        commentsRepository.getHintsIDs(stepID)

    suspend fun getCommentDetails(commentID: Long): Result<Comment> =
        commentsRepository.getCommentDetails(commentID)

    suspend fun abuseComment(commentID: Long) {
        commentsRepository.abuseComment(commentID)
    }

    suspend fun createReaction(commentID: Long, reaction: String) {
        commentsRepository.createReaction(commentID, reaction)
    }
}