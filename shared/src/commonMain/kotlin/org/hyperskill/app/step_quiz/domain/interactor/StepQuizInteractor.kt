package org.hyperskill.app.step_quiz.domain.interactor

import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepContext
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.model.attempts.AttemptStatus
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission
import org.hyperskill.app.step_quiz.domain.model.submissions.SubmissionStatus
import org.hyperskill.app.step_quiz.domain.repository.AttemptRepository
import org.hyperskill.app.step_quiz.domain.repository.SubmissionRepository

class StepQuizInteractor(
    private val attemptRepository: AttemptRepository,
    private val submissionRepository: SubmissionRepository
) {
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

    private suspend fun pollSubmission(submissionId: Long): Result<Submission> {
        while (true) {
            // delay(1000) TODO ALTAPPS-88 freezes indefinitely

            val submission = submissionRepository
                .getSubmissions(listOf(submissionId))
                .getOrThrow()
                .first()

            if (submission.status == SubmissionStatus.EVALUATION) {
                continue
            } else {
                return Result.success(submission)
            }
        }
    }

    fun isNeedRecreateAttemptForNewSubmission(step: Step): Boolean =
        when (step.block.name) {
            BlockName.CODE,
            BlockName.MATCHING,
            BlockName.SORTING,
            BlockName.MATH,
            BlockName.NUMBER,
            BlockName.STRING ->
                false
            else ->
                true
        }
}