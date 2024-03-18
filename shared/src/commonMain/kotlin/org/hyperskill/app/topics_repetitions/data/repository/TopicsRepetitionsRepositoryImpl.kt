package org.hyperskill.app.topics_repetitions.data.repository

import org.hyperskill.app.topics_repetitions.data.source.TopicsRepetitionsRemoteDataSource
import org.hyperskill.app.topics_repetitions.domain.model.TopicRepetition
import org.hyperskill.app.topics_repetitions.domain.model.TopicRepetitionStatistics
import org.hyperskill.app.topics_repetitions.domain.repository.TopicsRepetitionsRepository

internal class TopicsRepetitionsRepositoryImpl(
    private val topicsRepetitionsRemoteDataSource: TopicsRepetitionsRemoteDataSource
) : TopicsRepetitionsRepository {
    override suspend fun getTopicsRepetitions(
        pageSize: Int,
        page: Int,
        isInCurrentTrack: Boolean
    ): Result<List<TopicRepetition>> =
        topicsRepetitionsRemoteDataSource.getTopicsRepetitions(pageSize, page, isInCurrentTrack)

    override suspend fun getTopicsRepetitionStatistics(isInCurrentTrack: Boolean): Result<TopicRepetitionStatistics> =
        topicsRepetitionsRemoteDataSource.getTopicsRepetitionStatistics(isInCurrentTrack)
}