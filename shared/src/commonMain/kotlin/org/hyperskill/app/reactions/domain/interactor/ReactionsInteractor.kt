package org.hyperskill.app.reactions.domain.interactor

import org.hyperskill.app.reactions.domain.model.ReactionType
import org.hyperskill.app.reactions.domain.repository.ReactionsRepository
import org.hyperskill.app.reactions.remote.model.ReactionsResponse

class ReactionsInteractor(
    private val reactionsRepository: ReactionsRepository
) {
    suspend fun createReaction(commentId: Long, reaction: ReactionType): Result<ReactionsResponse> =
        reactionsRepository.createReaction(commentId, reaction)
}