package org.hyperskill.app.topics.domain.repository

import org.hyperskill.app.topics.domain.model.Topic

interface TopicsRepository {
    suspend fun getTopics(topicsIds: List<Long>): Result<List<Topic>>

    suspend fun getTopic(topicId: Long): Result<Topic> =
        kotlin.runCatching {
            getTopics(listOf(topicId)).getOrThrow().first()
        }
}