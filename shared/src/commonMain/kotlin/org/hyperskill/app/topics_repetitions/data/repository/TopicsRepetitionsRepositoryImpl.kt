package org.hyperskill.app.topics_repetitions.data.repository

import org.hyperskill.app.topics_repetitions.data.source.TopicsRepetitionsRemoteDataSource
import org.hyperskill.app.topics_repetitions.domain.model.TopicsRepetitions
import org.hyperskill.app.topics_repetitions.domain.repository.TopicsRepetitionsRepository

class TopicsRepetitionsRepositoryImpl(
    private val topicsRepetitionsRemoteDataSource: TopicsRepetitionsRemoteDataSource
) : TopicsRepetitionsRepository {
    override suspend fun getCurrentTrackTopicsRepetitions(): Result<TopicsRepetitions> =
        topicsRepetitionsRemoteDataSource.getTopicsRepetitions(isCurrentTrack = true)
}