package org.hyperskill.app.reactions.data.repository

import org.hyperskill.app.reactions.data.source.ReactionsRemoteDataSource
import org.hyperskill.app.reactions.domain.model.Reaction
import org.hyperskill.app.reactions.domain.model.ReactionType
import org.hyperskill.app.reactions.domain.repository.ReactionsRepository

internal class ReactionsRepositoryImpl(
    private val reactionsRemoteDataSource: ReactionsRemoteDataSource
) : ReactionsRepository {
    override suspend fun createCommentReaction(commentId: Long, reaction: ReactionType): Result<Reaction> =
        reactionsRemoteDataSource.createCommentReaction(commentId, reaction)

    override suspend fun removeCommentReaction(commentId: Long, reaction: ReactionType): Result<Unit> =
        reactionsRemoteDataSource.removeCommentReaction(commentId, reaction)
}