package org.hyperskill.app.topics.domain.repository

import org.hyperskill.app.topics.domain.model.Topic

interface TopicsRepository {
    suspend fun getTopics(topicsIds: List<Long>): Result<List<Topic>>
}