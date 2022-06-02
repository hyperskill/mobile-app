package org.hyperskill.app.step_quiz.domain.repository

import org.hyperskill.app.step.domain.model.StepContext
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission

interface SubmissionRepository {
    suspend fun getSubmissionsForStep(
        stepId: Long,
        userId: Long,
        page: Int = 1
    ): Result<List<Submission>>

    suspend fun getSubmissionsForAttempt(
        attemptId: Long,
        stepId: Long,
        userId: Long,
        page: Int = 1
    ): Result<List<Submission>> =
        getSubmissionsForStep(stepId, userId, page)
            .map { list -> list.filter { it.attempt == attemptId } }

    suspend fun getSubmissions(submissionsIds: List<Long>): Result<List<Submission>>

    suspend fun createSubmission(
        attemptId: Long,
        reply: Reply,
        solvingContext: StepContext = StepContext.DEFAULT
    ): Result<Submission>
}