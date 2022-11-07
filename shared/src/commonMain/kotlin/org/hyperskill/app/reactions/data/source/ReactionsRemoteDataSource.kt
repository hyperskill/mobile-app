package org.hyperskill.app.reactions.data.source

import org.hyperskill.app.reactions.domain.model.ReactionType
import org.hyperskill.app.reactions.remote.model.ReactionsResponse

interface ReactionsRemoteDataSource {
    suspend fun createReaction(commentId: Long, reaction: ReactionType): Result<ReactionsResponse>
}