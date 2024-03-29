package org.hyperskill.app.submissions.data.repository

import org.hyperskill.app.step.domain.model.StepContext
import org.hyperskill.app.submissions.data.source.SubmissionsCacheDataSource
import org.hyperskill.app.submissions.data.source.SubmissionsRemoteDataSource
import org.hyperskill.app.submissions.domain.model.Reply
import org.hyperskill.app.submissions.domain.model.Submission
import org.hyperskill.app.submissions.domain.repository.SubmissionsRepository

internal class SubmissionsRepositoryImpl(
    private val submissionsRemoteDataSource: SubmissionsRemoteDataSource,
    private val submissionsCacheDataSource: SubmissionsCacheDataSource
) : SubmissionsRepository {
    override suspend fun getSubmissionsForStep(
        stepId: Long,
        userId: Long,
        page: Int
    ): Result<List<Submission>> =
        submissionsRemoteDataSource.getSubmissionsForStep(stepId, userId, page)

    override suspend fun getSubmissions(submissionsIds: List<Long>): Result<List<Submission>> =
        submissionsRemoteDataSource.getSubmissions(submissionsIds)

    override suspend fun createSubmission(
        attemptId: Long,
        reply: Reply,
        solvingContext: StepContext
    ): Result<Submission> =
        submissionsRemoteDataSource.createSubmission(attemptId, reply, solvingContext)

    override suspend fun generateCodeWithErrors(stepId: Long): Result<String> =
        submissionsRemoteDataSource.generateCodeWithErrors(stepId)

    override fun incrementSolvedStepsCount() {
        submissionsCacheDataSource.incrementSolvedStepsCount()
    }

    override fun getSolvedStepsCount(): Long =
        submissionsCacheDataSource.getSolvedStepsCount()
}