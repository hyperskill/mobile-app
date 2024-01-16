package org.hyperskill.app.freemium.domain.interactor

import org.hyperskill.app.core.domain.repository.updateState
import org.hyperskill.app.profile.domain.model.isFreemiumIncreaseLimitsForFirstStepCompletionEnabled
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.subscriptions.domain.model.isProblemLimitReached
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository

class FreemiumInteractor(
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
    private val currentProfileStateRepository: CurrentProfileStateRepository
) {
    companion object {
        private const val SOLVING_FIRST_STEP_ADDITIONAL_LIMIT_VALUE = 10
    }

    suspend fun getStepsLimitTotal(): Result<Int?> =
        currentSubscriptionStateRepository.getState().map { it.stepsLimitTotal }

    suspend fun isProblemsLimitReached(): Result<Boolean> =
        kotlin.runCatching {
            currentSubscriptionStateRepository
                .getState()
                .getOrThrow()
                .isProblemLimitReached
        }

    suspend fun onStepSolved() {
        currentSubscriptionStateRepository.updateState { subscription ->
            if (subscription.type.areProblemsLimited) {
                subscription.copy(stepsLimitLeft = subscription.stepsLimitLeft?.dec())
            } else {
                subscription
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