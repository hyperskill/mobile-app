package org.hyperskill.app.comments.data.repository

import org.hyperskill.app.comments.data.source.CommentsRemoteDataSource
import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.domain.model.Like
import org.hyperskill.app.comments.domain.model.Reaction
import org.hyperskill.app.comments.domain.model.ReactionType
import org.hyperskill.app.comments.domain.repository.CommentsRepository

class CommentsRepositoryImpl(
    private val commentsRemoteDataSource: CommentsRemoteDataSource
) : CommentsRepository {
    override suspend fun getHintsIDs(stepId: Long): List<Long> =
        commentsRemoteDataSource
            .getDiscussions(
                targetType = "step",
                targetId = stepId,
                thread = "hint",
                ordering = "popular",
                isSpam = false
            )
            .getOrDefault(emptyList())
            .map { it.id }

    override suspend fun getCommentDetails(commentId: Long): Result<Comment> =
        commentsRemoteDataSource.getCommentDetails(commentId)

    override suspend fun abuseComment(commentId: Long): Result<Like> =
        commentsRemoteDataSource
            .createLike(
                subject = "abuse",
                targetType = "comment",
                targetId = commentId,
                value = 1
            )

    override suspend fun createReaction(commentId: Long, reaction: ReactionType): Result<Reaction> =
        commentsRemoteDataSource.createReaction(commentId, reaction.shortName)
}