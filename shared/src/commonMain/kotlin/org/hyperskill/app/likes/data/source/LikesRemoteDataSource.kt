package org.hyperskill.app.likes.data.source

import org.hyperskill.app.likes.domain.model.LikeSubject
import org.hyperskill.app.likes.domain.model.LikeValue
import org.hyperskill.app.likes.remote.model.LikesResponse

interface LikesRemoteDataSource {
    suspend fun createLike(
        targetType: String,
        targetId: Long,
        subject: LikeSubject,
        value: LikeValue
    ): Result<LikesResponse>
}