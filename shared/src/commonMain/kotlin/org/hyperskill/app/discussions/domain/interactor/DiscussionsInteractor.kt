package org.hyperskill.app.discussions.domain.interactor

import org.hyperskill.app.discussions.domain.model.Discussion
import org.hyperskill.app.discussions.domain.repository.DiscussionsRepository
import org.hyperskill.app.discussions.remote.model.DiscussionsRequest
import org.hyperskill.app.discussions.remote.model.DiscussionsResponse

class DiscussionsInteractor(
    private val discussionsRepository: DiscussionsRepository
) {
    /**
     * Executes a GET discussions request, with the specified **DiscussionsRequest**.
     *
     * @param request Uses for further configuring the HTTP request.
     * @return On success returns a value of [DiscussionsResponse] or a failure with an arbitrary Throwable exception.
     * @see Discussion
     */
    suspend fun getDiscussions(request: DiscussionsRequest): Result<DiscussionsResponse> =
        discussionsRepository.getDiscussions(request)
}