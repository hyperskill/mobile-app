package org.hyperskill.app.comments.data.source

import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.domain.model.Discussion
import org.hyperskill.app.comments.domain.model.Like
import org.hyperskill.app.comments.domain.model.Reaction

interface CommentsRemoteDataSource {
    suspend fun getDiscussions(
        targetType: String,
        targetId: Long,
        thread: String,
        ordering: String,
        isSpam: Boolean
    ): Result<List<Discussion>>

    suspend fun getCommentDetails(commentId: Long): Result<Comment>
    suspend fun createLike(subject: String, targetType: String, targetId: Long, value: Int): Result<Like>
    suspend fun createReaction(commentId: Long, shortName: String): Result<Reaction>
}