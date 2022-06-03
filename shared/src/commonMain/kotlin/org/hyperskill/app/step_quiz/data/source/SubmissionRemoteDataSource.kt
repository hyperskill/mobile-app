package org.hyperskill.app.step_quiz.data.source

import org.hyperskill.app.step.domain.model.StepContext
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission

interface SubmissionRemoteDataSource {
    suspend fun getSubmissionsForStep(stepId: Long, userId: Long, page: Int): Result<List<Submission>>

    suspend fun getSubmissionsForAttempt(attemptId: Long): Result<List<Submission>>

    suspend fun getSubmissions(submissionsIds: List<Long>): Result<List<Submission>>

    suspend fun createSubmission(attemptId: Long, reply: Reply, solvingContext: StepContext): Result<Submission>
}