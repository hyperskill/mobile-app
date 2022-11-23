package org.hyperskill.app.topics.data.source

import org.hyperskill.app.topics.domain.model.Topic

interface TopicsRemoteDataSource {
    suspend fun getTopics(topicsIds: List<Long>): Result<List<Topic>>
}