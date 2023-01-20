package org.hyperskill.app.topics.domain.interactor

import org.hyperskill.app.topics.domain.model.Topic
import org.hyperskill.app.topics.domain.repository.TopicsRepository

class TopicsInteractor(
    private val topicsRepository: TopicsRepository
) {
    suspend fun getTopics(topicsIds: List<Long>): Result<List<Topic>> =
        topicsRepository.getTopics(topicsIds)

    suspend fun getTopic(topicId: Long): Result<Topic> =
        kotlin.runCatching {
            topicsRepository
                .getTopics(listOf(topicId))
                .getOrThrow()
                .first()
        }
}