package org.hyperskill.app.step_quiz.data.repository

import org.hyperskill.app.step.domain.model.StepContext
import org.hyperskill.app.step_quiz.data.source.SubmissionCacheDataSource
import org.hyperskill.app.step_quiz.data.source.SubmissionRemoteDataSource
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission
import org.hyperskill.app.step_quiz.domain.repository.SubmissionRepository

internal class SubmissionRepositoryImpl(
    private val submissionRemoteDataSource: SubmissionRemoteDataSource,
    private val submissionCacheDataSource: SubmissionCacheDataSource
) : SubmissionRepository {
    override suspend fun getSubmissionsForStep(
        stepId: Long,
        userId: Long,
        page: Int
    ): Result<List<Submission>> =
        submissionRemoteDataSource.getSubmissionsForStep(stepId, userId, page)

    override suspend fun getSubmissions(submissionsIds: List<Long>): Result<List<Submission>> =
        submissionRemoteDataSource.getSubmissions(submissionsIds)

    override suspend fun createSubmission(
        attemptId: Long,
        reply: Reply,
        solvingContext: StepContext
    ): Result<Submission> =
        submissionRemoteDataSource.createSubmission(attemptId, reply, solvingContext)

    override fun incrementSolvedStepsCount() {
        submissionCacheDataSource.incrementSolvedStepsCount()
    }

    override fun getSolvedStepsCount(): Long =
        submissionCacheDataSource.getSolvedStepsCount()
}