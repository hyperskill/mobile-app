package org.hyperskill.app.topics_repetitions.domain.interactor

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.hyperskill.app.topics_repetitions.domain.model.TopicRepetition
import org.hyperskill.app.topics_repetitions.domain.model.TopicsRepetitionStatistics
import org.hyperskill.app.topics_repetitions.domain.repository.TopicsRepetitionsRepository

class TopicsRepetitionsInteractor(
    private val topicsRepetitionsRepository: TopicsRepetitionsRepository,
    val solvedStepsSharedFlow: SharedFlow<Long>
) {
    val topicRepeatedMutableSharedFlow: MutableSharedFlow<Unit> = MutableSharedFlow()

    val topicRepeatedSharedFlow: SharedFlow<Unit> = topicRepeatedMutableSharedFlow

    suspend fun getTopicsRepetitions(pageSize: Int, page: Int = 1): Result<List<TopicRepetition>> =
        topicsRepetitionsRepository.getTopicsRepetitions(pageSize, page)

    suspend fun getTopicsRepetitionStatistics(): Result<TopicsRepetitionStatistics> =
        topicsRepetitionsRepository.getTopicsRepetitionStatistics()
}