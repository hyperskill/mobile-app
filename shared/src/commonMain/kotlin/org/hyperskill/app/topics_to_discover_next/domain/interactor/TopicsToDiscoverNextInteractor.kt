package org.hyperskill.app.topics_to_discover_next.domain.interactor

import org.hyperskill.app.topics.domain.model.Topic

@Deprecated("TopicsToDiscoverNextInteractor is going to be removed.")
class TopicsToDiscoverNextInteractor {
    /**
     * Returns a list of topics to discover next.
     */
    suspend fun getTopicsToDiscoverNext(): Result<List<Topic>> =
        Result.success(emptyList())
}