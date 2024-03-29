package org.hyperskill.app.submissions.data.source

import org.hyperskill.app.step.domain.model.StepContext
import org.hyperskill.app.submissions.domain.model.Reply
import org.hyperskill.app.submissions.domain.model.Submission

interface SubmissionsRemoteDataSource {
    suspend fun getSubmissionsForStep(stepId: Long, userId: Long, page: Int): Result<List<Submission>>

    suspend fun getSubmissions(submissionsIds: List<Long>): Result<List<Submission>>

    suspend fun createSubmission(attemptId: Long, reply: Reply, solvingContext: StepContext): Result<Submission>

    suspend fun generateCodeWithErrors(stepId: Long): Result<String>
}