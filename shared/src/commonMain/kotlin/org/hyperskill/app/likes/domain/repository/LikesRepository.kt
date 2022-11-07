package org.hyperskill.app.likes.domain.repository

import org.hyperskill.app.likes.domain.model.LikeSubject
import org.hyperskill.app.likes.domain.model.LikeValue
import org.hyperskill.app.likes.remote.model.LikesResponse

interface LikesRepository {
    suspend fun createLike(
        targetType: String,
        targetId: Long,
        subject: LikeSubject,
        value: LikeValue
    ): Result<LikesResponse>

    suspend fun abuseComment(commentId: Long): Result<LikesResponse>
}