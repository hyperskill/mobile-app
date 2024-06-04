package org.hyperskill.app.step.domain.interactor

import kotlin.time.Duration
import org.hyperskill.app.progresses.domain.repository.ProgressesRepository
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepContext
import org.hyperskill.app.step.domain.model.isSupported
import org.hyperskill.app.step.domain.repository.StepRepository

class StepInteractor(
    private val stepRepository: StepRepository,
    private val progressesRepository: ProgressesRepository
) {
    suspend fun getStep(stepId: Long): Result<Step> =
        kotlin.runCatching {
            return stepRepository.getStep(stepId)
        }

    suspend fun getSteps(stepIds: List<Long>): Result<List<Step>> =
        kotlin.runCatching {
            return stepRepository.getSteps(stepIds)
        }

    suspend fun viewStep(stepId: Long, stepContext: StepContext) {
        stepRepository.viewStep(stepId, stepContext)
    }

    suspend fun logStepSolvingTime(stepId: Long, duration: Duration): Result<Unit> =
        stepRepository.logStepSolvingTime(stepId, duration)

    suspend fun getNextRecommendedStepAndCompleteCurrentIfNeeded(currentStep: Step): Result<Step?> =
        kotlin.runCatching {
            if (currentStep.topic == null) {
                return Result.failure(IllegalArgumentException("Current step doesn't have topic"))
            }

            if (currentStep.type == Step.Type.THEORY && !currentStep.isCompleted) {
                stepRepository.completeStep(currentStep.id)
                // Update topic progress, so that progress bar is updated
                progressesRepository
                    .getTopicProgress(currentStep.topic, forceLoadFromRemote = true)
                    .getOrThrow()
            }

            var nextRecommendedStep = stepRepository
                .getNextRecommendedStepByTopicId(currentStep.topic)
                .getOrThrow()

            while (nextRecommendedStep != null && !nextRecommendedStep.isSupported() && nextRecommendedStep.canSkip) {
                stepRepository
                    .skipStep(nextRecommendedStep.id)
                    .getOrThrow()

                nextRecommendedStep = stepRepository
                    .getNextRecommendedStepByTopicId(currentStep.topic)
                    .getOrThrow()
            }

            nextRecommendedStep
        }

    suspend fun skipStep(stepId: Long): Result<Unit> =
        stepRepository.skipStep(stepId)
}