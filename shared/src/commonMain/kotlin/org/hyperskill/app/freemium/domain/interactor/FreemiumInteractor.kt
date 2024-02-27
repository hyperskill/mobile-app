package org.hyperskill.app.freemium.domain.interactor

import org.hyperskill.app.core.domain.repository.updateState
import org.hyperskill.app.freemium.domain.model.FreemiumChargeLimitsStrategy
import org.hyperskill.app.profile.domain.model.isFreemiumIncreaseLimitsForFirstStepCompletionEnabled
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.profile.domain.repository.isFreemiumWrongSubmissionChargeLimitsEnabled
import org.hyperskill.app.subscriptions.domain.model.isFreemium
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository

class FreemiumInteractor(
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
    private val currentProfileStateRepository: CurrentProfileStateRepository
) {
    companion object {
        private const val SOLVING_FIRST_STEP_ADDITIONAL_LIMIT_VALUE = 10
    }

    suspend fun isFreemiumEnabled(): Result<Boolean> =
        currentSubscriptionStateRepository.getState().map { it.isFreemium }

    suspend fun getStepsLimitTotal(): Result<Int?> =
        currentSubscriptionStateRepository.getState().map { it.stepsLimitTotal }

    suspend fun isProblemsLimitReached(): Result<Boolean> =
        kotlin.runCatching {
            if (isFreemiumEnabled().getOrThrow()) {
                val subscription = currentSubscriptionStateRepository
                    .getState()
                    .getOrThrow()

                subscription.isFreemium && subscription.stepsLimitLeft == 0
            } else {
                false
            }
        }

    suspend fun chargeProblemsLimits(chargeStrategy: FreemiumChargeLimitsStrategy) {
        val isFreemiumEnabled = isFreemiumEnabled().getOrDefault(false)
        if (!isFreemiumEnabled) return

        when (chargeStrategy) {
            FreemiumChargeLimitsStrategy.AFTER_WRONG_SUBMISSION -> chargeLimitsAfterWrongSubmission()
            FreemiumChargeLimitsStrategy.AFTER_CORRECT_SUBMISSION -> chargeLimitsAfterCorrectSubmission()
        }
    }

    private suspend fun chargeLimitsAfterWrongSubmission() {
        if (currentProfileStateRepository.isFreemiumWrongSubmissionChargeLimitsEnabled()) {
            decreaseStepsLimitLeft()
        }
    }

    private suspend fun decreaseStepsLimitLeft() {
        currentSubscriptionStateRepository.getState().getOrNull()?.let {
            currentSubscriptionStateRepository.updateState(
                it.copy(stepsLimitLeft = it.stepsLimitLeft?.dec())
            )
        }
    }

    private suspend fun chargeLimitsAfterCorrectSubmission() {
        increaseLimitsForFirstStepCompletionIfNeeded()

        if (!currentProfileStateRepository.isFreemiumWrongSubmissionChargeLimitsEnabled()) {
            decreaseStepsLimitLeft()
        }
    }

    private suspend fun increaseLimitsForFirstStepCompletionIfNeeded() {
        val currentProfile = currentProfileStateRepository
            .getState(forceUpdate = false)
            .getOrElse { return }

        if (currentProfile.features.isFreemiumIncreaseLimitsForFirstStepCompletionEnabled &&
            currentProfile.gamification.passedProblems == 0
        ) {
            currentSubscriptionStateRepository.updateState {
                it.copy(
                    stepsLimitTotal = it.stepsLimitTotal?.plus(SOLVING_FIRST_STEP_ADDITIONAL_LIMIT_VALUE),
                    stepsLimitLeft = it.stepsLimitLeft?.plus(SOLVING_FIRST_STEP_ADDITIONAL_LIMIT_VALUE)
                )
            }
            currentProfileStateRepository.updateState {
                it.copy(gamification = it.gamification.copy(passedProblems = it.gamification.passedProblems + 1))
            }
        }
    }
}