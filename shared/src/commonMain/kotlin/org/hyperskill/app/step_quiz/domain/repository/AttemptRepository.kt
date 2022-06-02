package org.hyperskill.app.step_quiz.domain.repository

import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt

interface AttemptRepository {
    suspend fun getAttemptsForStep(stepId: Long, userId: Long): Result<List<Attempt>>

    suspend fun createAttemptForStep(stepId: Long): Result<Attempt>
}