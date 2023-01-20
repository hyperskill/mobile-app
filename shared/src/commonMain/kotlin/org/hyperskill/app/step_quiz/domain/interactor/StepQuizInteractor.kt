package org.hyperskill.app.step_quiz.domain.interactor

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
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
    val failedToLoadNextStepQuizMutableSharedFlow = MutableSharedFlow<Unit>()

    fun observeFailedToLoadNextStepQuiz(): SharedFlow<Unit> =
        failedToLoadNextStepQuizMutableSharedFlow

    companion object {
        private val POLL_SUBMISSION_INTERVAL = 1.seconds
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

            val evaluatedSubmission = if (submission.status == SubmissionStatus.EVALUATION) {
                pollSubmission(submission.id)
            } else {
                submission
            }

            if (evaluatedSubmission.status == SubmissionStatus.CORRECT) {
                submissionRepository.notifyStepSolved(stepId)
            }

            return Result.success(evaluatedSubmission)
        }

    private suspend fun pollSubmission(submissionId: Long, retryCount: Int = 1): Submission {
        delay(POLL_SUBMISSION_INTERVAL * retryCount)

        val submission = submissionRepository
            .getSubmissions(listOf(submissionId))
            .getOrThrow()
            .first()

        return if (submission.status == SubmissionStatus.EVALUATION) {
            pollSubmission(submissionId = submissionId, retryCount = retryCount + 1)
        } else {
            submission
        }
    }
}