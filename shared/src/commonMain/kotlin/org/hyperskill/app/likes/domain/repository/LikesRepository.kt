package org.hyperskill.app.likes.domain.repository

import org.hyperskill.app.content_type.domain.model.ContentType
import org.hyperskill.app.likes.domain.model.Like
import org.hyperskill.app.likes.domain.model.LikeSubject
import org.hyperskill.app.likes.domain.model.LikeValue

interface LikesRepository {
    suspend fun createLike(
        targetType: ContentType,
        targetId: Long,
        subject: LikeSubject,
        value: LikeValue
    ): Result<Like>

    suspend fun abuseComment(commentId: Long): Result<Like>
}