package org.hyperskill.app.request_review.domain.interactor

import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.datetime.Clock
import org.hyperskill.app.request_review.domain.repository.RequestReviewRepository
import org.hyperskill.app.step_quiz.domain.repository.SubmissionRepository

class RequestReviewInteractor(
    private val requestReviewRepository: RequestReviewRepository,
    private val submissionRepository: SubmissionRepository
) {
    companion object {
        private const val SOLVED_STEPS_FREQUENCY_TO_REQUEST_REVIEW = 10
        private const val MAX_REQUEST_REVIEW_COUNT = 3
        private val ONE_WEEK_IN_MILLIS = 7.toDuration(DurationUnit.DAYS).inWholeMilliseconds
    }

    fun shouldRequestReviewAfterStepSolved(): Boolean {
        val solvedStepsCount = submissionRepository.getSolvedStepsCount()
        val isPassedSolvedStepsFrequency = solvedStepsCount % SOLVED_STEPS_FREQUENCY_TO_REQUEST_REVIEW == 0L
        if (!isPassedSolvedStepsFrequency) {
            return false
        }

        val requestReviewCount = requestReviewRepository.getRequestReviewCount()
        if (requestReviewCount >= MAX_REQUEST_REVIEW_COUNT) {
            return false
        }

        // Request review once a week
        val lastRequestReviewTimestamp = requestReviewRepository.getLastRequestReviewTimestamp() ?: return true
        return (lastRequestReviewTimestamp + ONE_WEEK_IN_MILLIS) <= Clock.System.now().toEpochMilliseconds()
    }

    fun handleRequestReviewPerformed() {
        requestReviewRepository.incrementRequestReviewCount()
        requestReviewRepository.setLastRequestReviewTimestamp(Clock.System.now().toEpochMilliseconds())
    }
}