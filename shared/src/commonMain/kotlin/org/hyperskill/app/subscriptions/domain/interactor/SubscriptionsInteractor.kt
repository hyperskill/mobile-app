package org.hyperskill.app.subscriptions.domain.interactor

import co.touchlab.kermit.Logger
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.core.domain.repository.updateState
import org.hyperskill.app.profile.domain.model.isFreemiumIncreaseLimitsForFirstStepCompletionEnabled
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.model.SubscriptionType
import org.hyperskill.app.subscriptions.domain.model.areProblemsLimited
import org.hyperskill.app.subscriptions.domain.model.isActive
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import org.hyperskill.app.subscriptions.domain.repository.SubscriptionsRepository

class SubscriptionsInteractor(
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val subscriptionsRepository: SubscriptionsRepository,
    private val authInteractor: AuthInteractor,
    logger: Logger
) {
    companion object {
        private const val LOG_TAG = "SubscriptionsInteractor"
        private const val SOLVING_FIRST_STEP_ADDITIONAL_LIMIT_VALUE = 10
    }

    private val logger: Logger = logger.withTag(LOG_TAG)

    private var refreshMobileOnlySubscriptionJob: Job? = null

    suspend fun onStepSolved() {
        currentSubscriptionStateRepository.updateState { subscription ->
            if (subscription.areProblemsLimited) {
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

    suspend fun refreshSubscriptionOnExpirationIfNeeded(subscription: Subscription) {
        refreshMobileOnlySubscriptionJob?.cancel()
        refreshMobileOnlySubscriptionJob = null

        val isActiveMobileOnlySubscription =
            subscription.type == SubscriptionType.MOBILE_ONLY && subscription.isActive

        if (isActiveMobileOnlySubscription && subscription.validTill != null) {
            coroutineScope {
                refreshMobileOnlySubscriptionJob = launch {
                    refreshMobileOnlySubscriptionOnExpiration(subscription.validTill)
                }
                refreshMobileOnlySubscriptionJob?.invokeOnCompletion {
                    refreshMobileOnlySubscriptionJob = null
                }
            }
        }
    }

    private suspend fun refreshMobileOnlySubscriptionOnExpiration(
        subscriptionValidTill: Instant
    ) {
        val nowByUTC = Clock.System.now()
            .toLocalDateTime(TimeZone.UTC)
            .toInstant(TimeZone.UTC)
        val delayDuration = subscriptionValidTill - nowByUTC
        logger.d { "Wait ${delayDuration.inWholeSeconds} seconds for subscription expiration to refresh it" }
        delay(delayDuration)
        if (isUserAuthorized()) {
            subscriptionsRepository
                .syncSubscription()
                .onSuccess { freshSubscription ->
                    currentSubscriptionStateRepository.updateState(freshSubscription)
                    logger.d {
                        """
                            Subscription successfully refreshed.
                            Type=${freshSubscription.type},status=${freshSubscription.status}
                        """.trimMargin() }
                }
                .onFailure { e ->
                    logger.e(e) { "Failed to refresh subscription" }
                }
        }
    }

    fun cancelSubscriptionRefresh() {
        refreshMobileOnlySubscriptionJob?.cancel()
        refreshMobileOnlySubscriptionJob = null
    }

    private suspend fun isUserAuthorized(): Boolean =
        authInteractor.isAuthorized().getOrDefault(false)
}