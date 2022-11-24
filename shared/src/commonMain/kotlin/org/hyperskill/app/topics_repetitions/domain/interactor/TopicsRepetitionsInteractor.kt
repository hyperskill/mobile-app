package org.hyperskill.app.topics_repetitions.domain.interactor

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.hyperskill.app.topics_repetitions.domain.model.TopicsRepetitions
import org.hyperskill.app.topics_repetitions.domain.repository.TopicsRepetitionsRepository

class TopicsRepetitionsInteractor(
    private val topicsRepetitionsRepository: TopicsRepetitionsRepository,
    solvedStepsMutableSharedFlow: MutableSharedFlow<Long>
) {
    suspend fun getCurrentTrackTopicsRepetitions(): Result<TopicsRepetitions> =
        topicsRepetitionsRepository.getCurrentTrackTopicsRepetitions()

    val solvedStepsSharedFlow: SharedFlow<Long> = solvedStepsMutableSharedFlow
}