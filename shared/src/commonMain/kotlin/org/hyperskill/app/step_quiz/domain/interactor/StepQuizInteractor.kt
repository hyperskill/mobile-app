package org.hyperskill.app.step_quiz.domain.interactor

import kotlinx.coroutines.delay
import org.hyperskill.app.step.domain.model.StepContext
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.model.attempts.AttemptStatus
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission
import org.hyperskill.app.step_quiz.domain.model.submissions.SubmissionStatus
import org.hyperskill.app.step_quiz.domain.repository.AttemptRepository
import org.hyperskill.app.step_quiz.domain.repository.SubmissionRepository
import kotlin.time.Duration.Companion.seconds

class StepQuizInteractor(
    private val attemptRepository: AttemptRepository,
    private val submissionRepository: SubmissionRepository
) {
    companion object {
        const val POLL_SECONDS_INTERVAL = 0.5
    }

    suspend fun getAttempt(stepId: Long, userId: Long): Result<Attempt> =
        kotlin.runCatching {
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

    suspend fun getSubmission(attemptId: Long, stepId: Long, userId: Long): Result<Submission?> =
        submissionRepository
            .getSubmissionsForAttempt(attemptId, stepId, userId)
            .map { it.firstOrNull() }

    suspend fun createSubmission(
        stepId: Long,
        attemptId: Long,
        reply: Reply,
        solvingContext: StepContext = StepContext.DEFAULT
    ): Result<Submission> =
        kotlin.runCatching {
            val submission = submissionRepository
                .createSubmission(attemptId, reply, solvingContext)
                .getOrThrow()

            val pollSub = pollSubmission(submission.id)
            if (pollSub.getOrNull()?.status == SubmissionStatus.CORRECT) {
                submissionRepository.notifyStepSolved(stepId)
            }

            return pollSub
        }

    private suspend fun pollSubmission(submissionId: Long, retryCount: Int = 1): Result<Submission> {
        delay(POLL_SECONDS_INTERVAL.seconds * retryCount)

        val submission = submissionRepository
            .getSubmissions(listOf(submissionId))
            .getOrThrow()
            .first()

        return if (submission.status == SubmissionStatus.EVALUATION) {
            pollSubmission(submissionId = submissionId, retryCount = retryCount + 1)
        } else {
            Result.success(submission)
        }
    }
}