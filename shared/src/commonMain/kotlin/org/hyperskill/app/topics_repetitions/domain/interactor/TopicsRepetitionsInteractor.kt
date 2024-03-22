package org.hyperskill.app.topics_repetitions.domain.interactor

import org.hyperskill.app.topics_repetitions.domain.model.TopicRepetition
import org.hyperskill.app.topics_repetitions.domain.model.TopicRepetitionStatistics
import org.hyperskill.app.topics_repetitions.domain.repository.TopicsRepetitionsRepository

class TopicsRepetitionsInteractor(
    private val topicsRepetitionsRepository: TopicsRepetitionsRepository
) {
    suspend fun getTopicsRepetitions(
        pageSize: Int,
        page: Int = 1,
        isInCurrentTrack: Boolean = true
    ): Result<List<TopicRepetition>> =
        topicsRepetitionsRepository.getTopicsRepetitions(pageSize, page, isInCurrentTrack)

    suspend fun getTopicsRepetitionStatistics(isInCurrentTrack: Boolean = true): Result<TopicRepetitionStatistics> =
        topicsRepetitionsRepository.getTopicsRepetitionStatistics(isInCurrentTrack)
}