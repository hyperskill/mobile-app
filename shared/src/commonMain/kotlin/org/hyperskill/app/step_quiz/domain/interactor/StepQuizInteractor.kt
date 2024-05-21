package org.hyperskill.app.step_quiz.domain.interactor

import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import org.hyperskill.app.step.domain.model.StepContext
import org.hyperskill.app.step_completion.domain.flow.StepCompletedFlow
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.model.attempts.AttemptStatus
import org.hyperskill.app.step_quiz.domain.repository.AttemptRepository
import org.hyperskill.app.submissions.domain.model.Reply
import org.hyperskill.app.submissions.domain.model.Submission
import org.hyperskill.app.submissions.domain.model.SubmissionStatus
import org.hyperskill.app.submissions.domain.repository.SubmissionsRepository

class StepQuizInteractor(
    private val attemptRepository: AttemptRepository,
    private val submissionsRepository: SubmissionsRepository,
    private val stepCompletedFlow: StepCompletedFlow
) {
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
        submissionsRepository
            .getSubmissionsForAttempt(attemptId, stepId, userId)
            .map { it.firstOrNull() }

    suspend fun createSubmission(
        stepId: Long,
        attemptId: Long,
        reply: Reply,
        solvingContext: StepContext
    ): Result<Submission> =
        kotlin.runCatching {
            val submission = submissionsRepository
                .createSubmission(attemptId, reply, solvingContext)
                .getOrThrow()

            val evaluatedSubmission = if (submission.status == SubmissionStatus.EVALUATION) {
                pollSubmission(submission.id)
            } else {
                submission
            }

            if (evaluatedSubmission.status == SubmissionStatus.CORRECT) {
                submissionsRepository.incrementSolvedStepsCount()
                stepCompletedFlow.notifyDataChanged(stepId)
            }

            return Result.success(evaluatedSubmission)
        }

    private suspend fun pollSubmission(submissionId: Long, retryCount: Int = 1): Submission {
        delay(POLL_SUBMISSION_INTERVAL * retryCount)

        val submission = submissionsRepository
            .getSubmissions(listOf(submissionId))
            .getOrThrow()
            .first()

        return if (submission.status == SubmissionStatus.EVALUATION) {
            pollSubmission(submissionId = submissionId, retryCount = retryCount + 1)
        } else {
            submission
        }
    }

    suspend fun generateGptCodeWithErrors(stepId: Long): Result<String> =
        submissionsRepository.generateCodeWithErrors(stepId).map { code ->
            code.lineSequence().filter { !it.startsWith("```") }.joinToString("\n")
        }
}