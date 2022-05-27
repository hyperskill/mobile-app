package org.hyperskill.app.step_quiz.data.repository

import org.hyperskill.app.step_quiz.data.source.AttemptRemoteDataSource
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.repository.AttemptRepository

class AttemptRepositoryImpl(
    private val attemptRemoteDataSource: AttemptRemoteDataSource
) : AttemptRepository {
    override suspend fun getAttemptsForStep(stepId: Long, userId: Long): Result<List<Attempt>> =
        attemptRemoteDataSource.getAttemptsForStep(stepId, userId)

    override suspend fun createAttemptForStep(stepId: Long): Result<Attempt> =
        attemptRemoteDataSource.createAttemptForStep(stepId)
}