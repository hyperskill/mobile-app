package org.hyperskill.app.step_quiz.data.source

import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt

interface AttemptRemoteDataSource {
    suspend fun getAttemptsForStep(stepId: Long, userId: Long): Result<List<Attempt>>

    suspend fun createAttemptForStep(stepId: Long): Result<Attempt>
}