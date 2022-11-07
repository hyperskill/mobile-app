package org.hyperskill.app.likes.data.repository

import org.hyperskill.app.likes.data.source.LikesRemoteDataSource
import org.hyperskill.app.likes.domain.model.LikeSubject
import org.hyperskill.app.likes.domain.model.LikeValue
import org.hyperskill.app.likes.domain.repository.LikesRepository
import org.hyperskill.app.likes.remote.model.LikesResponse

class LikesRepositoryImpl(
    private val likesRemoteDataSource: LikesRemoteDataSource
) : LikesRepository {
    override suspend fun createLike(
        targetType: String,
        targetId: Long,
        subject: LikeSubject,
        value: LikeValue
    ): Result<LikesResponse> =
        likesRemoteDataSource.createLike(targetType, targetId, subject, value)

    override suspend fun abuseComment(commentId: Long): Result<LikesResponse> =
        likesRemoteDataSource.createLike(
            targetType = "comment",
            targetId = commentId,
            subject = LikeSubject.ABUSE,
            value = LikeValue.VALUE_1
        )
}