package org.hyperskill.app.step_quiz.domain.interactor

import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.model.attempts.AttemptStatus
import org.hyperskill.app.step_quiz.domain.repository.AttemptRepository

class StepQuizInteractor(
    private val attemptRepository: AttemptRepository
) {
    suspend fun getAttempt(stepId: Long, userId: Long): Result<Attempt> {
        val activeAttempt = attemptRepository
            .getAttemptsForStep(stepId, userId)
            .getOrThrow()
            .firstOrNull { it.status == AttemptStatus.ACTIVE }

        return if (activeAttempt != null) {
            Result.success(activeAttempt)
        } else {
            createAttempt(stepId)
        }
    }

    suspend fun createAttempt(stepId: Long): Result<Attempt> =
        attemptRepository.createAttemptForStep(stepId)
}