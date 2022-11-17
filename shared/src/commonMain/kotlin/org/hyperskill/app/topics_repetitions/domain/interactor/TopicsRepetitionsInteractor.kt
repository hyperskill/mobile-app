package org.hyperskill.app.topics_repetitions.domain.interactor

import org.hyperskill.app.topics_repetitions.domain.model.TopicsRepetitions
import org.hyperskill.app.topics_repetitions.domain.repository.TopicsRepetitionsRepository

class TopicsRepetitionsInteractor(
    private val topicsRepetitionsRepository: TopicsRepetitionsRepository
) {
    suspend fun getCurrentTrackTopicsRepetitions(): Result<TopicsRepetitions> =
        topicsRepetitionsRepository.getCurrentTrackTopicsRepetitions()
}