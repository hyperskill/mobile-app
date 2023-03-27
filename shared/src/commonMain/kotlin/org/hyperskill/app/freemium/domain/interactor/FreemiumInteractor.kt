package org.hyperskill.app.freemium.domain.interactor

import org.hyperskill.app.subscriptions.domain.model.isFreemium
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository

class FreemiumInteractor(
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository
) {
    suspend fun isFreemiumEnabled(): Result<Boolean> =
        currentSubscriptionStateRepository.getState().map { it.isFreemium }

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
    }
}