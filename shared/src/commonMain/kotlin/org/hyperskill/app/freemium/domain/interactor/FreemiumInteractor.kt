package org.hyperskill.app.freemium.domain.interactor

import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.profile.domain.model.isFreemiumFeatureEnabled
import org.hyperskill.app.subscriptions.domain.model.isFree
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository

class FreemiumInteractor(
    private val profileInteractor: ProfileInteractor,
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository
) {
    suspend fun isFreemiumEnabled(): Result<Boolean> =
        kotlin.runCatching {
            profileInteractor.getCurrentProfile().getOrThrow().isFreemiumFeatureEnabled &&
                currentSubscriptionStateRepository.getState().getOrThrow().isFree
        }

    suspend fun isProblemsLimitReached(): Result<Boolean> =
        kotlin.runCatching {
            if (isFreemiumEnabled().getOrThrow()) {
                val subscription = currentSubscriptionStateRepository
                    .getState()
                    .getOrThrow()

                subscription.isFree && subscription.stepsLimitLeft == 0
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