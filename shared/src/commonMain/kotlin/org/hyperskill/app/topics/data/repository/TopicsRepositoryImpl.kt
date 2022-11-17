package org.hyperskill.app.topics.data.repository

import org.hyperskill.app.topics.data.source.TopicsRemoteDataSource
import org.hyperskill.app.topics.domain.model.Topic
import org.hyperskill.app.topics.domain.repository.TopicsRepository

class TopicsRepositoryImpl(
    private val topicsRemoteDataSource: TopicsRemoteDataSource
) : TopicsRepository {
    override suspend fun getTopicsByIds(topicsIds: List<Long>): Result<List<Topic>> =
        topicsRemoteDataSource.getTopicsByIds(topicsIds)
}