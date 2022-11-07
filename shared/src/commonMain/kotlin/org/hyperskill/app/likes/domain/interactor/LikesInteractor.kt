package org.hyperskill.app.likes.domain.interactor

import org.hyperskill.app.likes.domain.model.LikeSubject
import org.hyperskill.app.likes.domain.model.LikeValue
import org.hyperskill.app.likes.domain.repository.LikesRepository
import org.hyperskill.app.likes.remote.model.LikesResponse

class LikesInteractor(
    private val likesRepository: LikesRepository
) {
    suspend fun createLike(
        targetType: String,
        targetId: Long,
        subject: LikeSubject,
        value: LikeValue
    ): Result<LikesResponse> =
        likesRepository.createLike(targetType, targetId, subject, value)

    suspend fun abuseComment(commentId: Long): Result<LikesResponse> =
        likesRepository.abuseComment(commentId)
}