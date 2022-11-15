package org.hyperskill.app.reactions.data.source

import org.hyperskill.app.reactions.domain.model.Reaction
import org.hyperskill.app.reactions.domain.model.ReactionType

interface ReactionsRemoteDataSource {
    suspend fun createCommentReaction(commentId: Long, reaction: ReactionType): Result<Reaction>
}