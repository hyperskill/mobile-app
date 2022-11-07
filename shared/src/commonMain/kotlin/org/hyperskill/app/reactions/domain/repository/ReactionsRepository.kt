package org.hyperskill.app.reactions.domain.repository

import org.hyperskill.app.reactions.domain.model.ReactionType
import org.hyperskill.app.reactions.remote.model.ReactionsResponse

interface ReactionsRepository {
    suspend fun createReaction(commentId: Long, reaction: ReactionType): Result<ReactionsResponse>
}