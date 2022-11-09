package org.hyperskill.app.comments.domain.interactor

import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.domain.model.Like
import org.hyperskill.app.comments.domain.model.Reaction
import org.hyperskill.app.comments.domain.model.ReactionType
import org.hyperskill.app.comments.domain.repository.CommentsRepository

class CommentsDataInteractor(
    private val commentsRepository: CommentsRepository
) {
    suspend fun getHintsIds(stepId: Long): List<Long> =
        commentsRepository.getHintsIDs(stepId)

    suspend fun getCommentDetails(commentId: Long): Result<Comment> =
        commentsRepository.getCommentDetails(commentId)

    suspend fun abuseComment(commentId: Long): Result<Like> =
        commentsRepository.abuseComment(commentId)

    suspend fun createReaction(commentId: Long, reaction: ReactionType): Result<Reaction> =
        commentsRepository.createReaction(commentId, reaction)
}