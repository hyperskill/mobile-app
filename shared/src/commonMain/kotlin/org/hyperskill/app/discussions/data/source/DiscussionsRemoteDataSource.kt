package org.hyperskill.app.discussions.data.source

import org.hyperskill.app.discussions.remote.model.DiscussionsRequest
import org.hyperskill.app.discussions.remote.model.DiscussionsResponse

interface DiscussionsRemoteDataSource {
    suspend fun getDiscussions(request: DiscussionsRequest): Result<DiscussionsResponse>
}