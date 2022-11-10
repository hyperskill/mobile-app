package org.hyperskill.app.reactions.domain.interactor

import org.hyperskill.app.reactions.domain.model.Reaction
import org.hyperskill.app.reactions.domain.model.ReactionType
import org.hyperskill.app.reactions.domain.repository.ReactionsRepository

class ReactionsInteractor(
    private val reactionsRepository: ReactionsRepository
) {
    suspend fun createCommentReaction(commentId: Long, reaction: ReactionType): Result<Reaction> =
        reactionsRepository.createCommentReaction(commentId, reaction)
}