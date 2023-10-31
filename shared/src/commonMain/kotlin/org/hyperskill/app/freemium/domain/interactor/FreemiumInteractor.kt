package org.hyperskill.app.freemium.domain.interactor

import org.hyperskill.app.core.domain.repository.updateState
import org.hyperskill.app.profile.domain.model.isFreemiumIncreaseLimitsForFirstStepCompletionEnabled
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
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

    suspend fun onStepSolved() {
        if (isFreemiumEnabled().getOrDefault(false)) {
            currentSubscriptionStateRepository.getState().getOrNull()?.let {
                currentSubscriptionStateRepository.updateState(
                    it.copy(stepsLimitLeft = it.stepsLimitLeft?.dec())
                )
            }
        }
        currentProfileStateRepository.getState().onSuccess { currentProfile ->
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
}