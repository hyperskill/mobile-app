package org.hyperskill.app.comments.data.source

import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.domain.model.Discussion
import org.hyperskill.app.comments.domain.model.Like

interface CommentsRemoteDataSource {
    suspend fun getDiscussions(targetType: String, targetID: Long, thread: String, ordering: String, isSpam: Boolean): Result<List<Discussion>>
    suspend fun getCommentDetails(commentID: Long): Result<Comment>
    suspend fun createLike(subject: String, targetType: String, targetID: Long, value: Int): Result<Like>
    suspend fun createReaction(commentID: Long, shortName: String)
}