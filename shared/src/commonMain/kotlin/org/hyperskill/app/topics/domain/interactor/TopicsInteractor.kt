package org.hyperskill.app.topics.domain.interactor

import org.hyperskill.app.topics.domain.model.Topic
import org.hyperskill.app.topics.domain.repository.TopicsRepository

class TopicsInteractor(
    private val topicsRepository: TopicsRepository
) {
    suspend fun getTopicsByIds(topicsIds: List<Long>): Result<List<Topic>> =
        topicsRepository.getTopicsByIds(topicsIds)
}