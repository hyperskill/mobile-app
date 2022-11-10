package org.hyperskill.app.likes.data.source

import org.hyperskill.app.likes.domain.model.Like
import org.hyperskill.app.likes.domain.model.LikeSubject
import org.hyperskill.app.likes.domain.model.LikeValue

interface LikesRemoteDataSource {
    suspend fun createLike(
        targetType: String,
        targetId: Long,
        subject: LikeSubject,
        value: LikeValue
    ): Result<Like>
}