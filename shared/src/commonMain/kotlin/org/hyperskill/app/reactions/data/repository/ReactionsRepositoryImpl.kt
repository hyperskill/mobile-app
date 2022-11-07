package org.hyperskill.app.reactions.data.repository

import org.hyperskill.app.reactions.data.source.ReactionsRemoteDataSource
import org.hyperskill.app.reactions.domain.model.ReactionType
import org.hyperskill.app.reactions.domain.repository.ReactionsRepository
import org.hyperskill.app.reactions.remote.model.ReactionsResponse

class ReactionsRepositoryImpl(
    private val reactionsRemoteDataSource: ReactionsRemoteDataSource
) : ReactionsRepository {
    override suspend fun createReaction(commentId: Long, reaction: ReactionType): Result<ReactionsResponse> =
        reactionsRemoteDataSource.createReaction(commentId, reaction)
}