package org.hyperskill.app.topics_repetitions.data.repository

import org.hyperskill.app.topics_repetitions.data.source.TopicsRepetitionsRemoteDataSource
import org.hyperskill.app.topics_repetitions.domain.model.TopicRepetition
import org.hyperskill.app.topics_repetitions.domain.model.TopicsRepetitionStatistics
import org.hyperskill.app.topics_repetitions.domain.repository.TopicsRepetitionsRepository

class TopicsRepetitionsRepositoryImpl(
    private val topicsRepetitionsRemoteDataSource: TopicsRepetitionsRemoteDataSource
) : TopicsRepetitionsRepository {
    override suspend fun getTopicsRepetitions(pageSize: Int, page: Int): Result<List<TopicRepetition>> =
        topicsRepetitionsRemoteDataSource.getTopicsRepetitions(pageSize, page)

    override suspend fun getTopicsRepetitionStatistics(): Result<TopicsRepetitionStatistics> =
        topicsRepetitionsRemoteDataSource.getTopicsRepetitionStatistics()
}