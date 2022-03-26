package org.hyperskill.app.step.domain.interactor

import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.repository.StepRepository

class StepInteractor(
    private val stepRepository: StepRepository
) {
    suspend fun getStep(stepId: Long): Result<Step> =
        stepRepository.getStep(stepId)

    suspend fun getSteps(stepIds: List<Long>): Result<List<Step>> =
        stepRepository.getSteps(stepIds)
}