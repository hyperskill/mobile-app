package org.hyperskill.app.step.domain.interactor

import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.supportedBlocksNames
import org.hyperskill.app.step.domain.repository.StepRepository

class StepInteractor(
    private val stepRepository: StepRepository
) {
    suspend fun getStep(stepId: Long): Result<Step> =
        kotlin.runCatching {
            return stepRepository.getStep(stepId)
        }

    suspend fun getSteps(stepIds: List<Long>): Result<List<Step>> =
        kotlin.runCatching {
            return stepRepository.getSteps(stepIds)
        }

    suspend fun getNextRecommendedStepByCurrentStep(currentStep: Step): Result<Step> =
        kotlin.runCatching {
            if (currentStep.type == Step.Type.THEORY && !currentStep.isCompleted) {
                stepRepository.completeStep(currentStep.id)
            }

            var nextRecommendedStep = stepRepository
                .getNextRecommendedStepByTopicId(currentStep.topic)
                .getOrThrow()

            while (!BlockName.supportedBlocksNames.contains(nextRecommendedStep.block.name) && nextRecommendedStep.canSkip) {
                stepRepository
                    .completeStep(currentStep.id)
                    .getOrThrow()

                nextRecommendedStep = stepRepository
                    .getNextRecommendedStepByTopicId(currentStep.topic)
                    .getOrThrow()
            }

            nextRecommendedStep
        }
}