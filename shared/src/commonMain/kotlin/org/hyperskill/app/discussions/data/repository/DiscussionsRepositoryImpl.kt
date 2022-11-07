package org.hyperskill.app.discussions.data.repository

import org.hyperskill.app.discussions.data.source.DiscussionsRemoteDataSource
import org.hyperskill.app.discussions.domain.repository.DiscussionsRepository
import org.hyperskill.app.discussions.remote.model.DiscussionsRequest
import org.hyperskill.app.discussions.remote.model.DiscussionsResponse

class DiscussionsRepositoryImpl(
    private val discussionsRemoteDataSource: DiscussionsRemoteDataSource
) : DiscussionsRepository {
    override suspend fun getDiscussions(request: DiscussionsRequest): Result<DiscussionsResponse> =
        discussionsRemoteDataSource.getDiscussions(request)
}