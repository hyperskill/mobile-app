package org.hyperskill.app.reactions.domain.repository

import org.hyperskill.app.reactions.domain.model.Reaction
import org.hyperskill.app.reactions.domain.model.ReactionType

interface ReactionsRepository {
    suspend fun createCommentReaction(commentId: Long, reaction: ReactionType): Result<Reaction>
}