package org.hyperskill.app.likes.domain.interactor

import org.hyperskill.app.core.domain.model.ContentType
import org.hyperskill.app.likes.domain.model.Like
import org.hyperskill.app.likes.domain.model.LikeSubject
import org.hyperskill.app.likes.domain.model.LikeValue
import org.hyperskill.app.likes.domain.repository.LikesRepository

class LikesInteractor(
    private val likesRepository: LikesRepository
) {
    suspend fun createLike(
        targetType: ContentType,
        targetId: Long,
        subject: LikeSubject,
        value: LikeValue
    ): Result<Like> =
        likesRepository.createLike(targetType, targetId, subject, value)

    suspend fun abuseComment(commentId: Long): Result<Like> =
        likesRepository.abuseComment(commentId)
}