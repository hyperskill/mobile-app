package org.hyperskill.app.comments.data.repository

import org.hyperskill.app.comments.data.source.CommentsRemoteDataSource
import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.domain.model.Like
import org.hyperskill.app.comments.domain.model.ReactionType
import org.hyperskill.app.comments.domain.repository.CommentsRepository

class CommentsRepositoryImpl(
    private val commentsRemoteDataSource: CommentsRemoteDataSource
) : CommentsRepository {
    override suspend fun getHintsIDs(stepID: Long): List<Long> =
        commentsRemoteDataSource
            .getDiscussions(
                targetType = "step",
                targetID = stepID,
                thread = "hint",
                ordering = "popular",
                isSpam = false
            )
            .getOrDefault(emptyList())
            .map { it.id }

    override suspend fun getCommentDetails(commentID: Long): Result<Comment> =
        commentsRemoteDataSource.getCommentDetails(commentID)

    override suspend fun abuseComment(commentID: Long): Result<Like> =
        commentsRemoteDataSource
            .createLike(
                subject = "abuse",
                targetType = "comment",
                targetID = commentID,
                value = 1
            )

    override suspend fun createReaction(commentID: Long, reaction: ReactionType) {
        commentsRemoteDataSource.createReaction(commentID = commentID, shortName = reaction.shortName)
    }
}