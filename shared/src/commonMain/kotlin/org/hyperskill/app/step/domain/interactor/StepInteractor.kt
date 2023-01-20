package org.hyperskill.app.step.domain.interactor

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.repository.StepRepository

class StepInteractor(
    private val stepRepository: StepRepository
) {
    val readyToLoadNextStepQuizMutableSharedFlow = MutableSharedFlow<Step>()
    fun observeReadyToLoadNextStepQuiz(): SharedFlow<Step> =
        readyToLoadNextStepQuizMutableSharedFlow

    suspend fun getStep(stepId: Long): Result<Step> =
        kotlin.runCatching {
            return stepRepository.getStep(stepId)
        }

    suspend fun getSteps(stepIds: List<Long>): Result<List<Step>> =
        kotlin.runCatching {
            return stepRepository.getSteps(stepIds)
        }

    suspend fun completeStep(stepId: Long): Result<Step> =
        stepRepository.completeStep(stepId)

    suspend fun getNextRecommendedStepByTopicId(topicId: Long): Result<Step> =
        stepRepository.getNextRecommendedStepByTopicId(topicId)
}