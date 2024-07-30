package org.hyperskill.app.subscriptions.domain.interactor

import co.touchlab.kermit.Logger
import kotlin.time.DurationUnit
import kotlin.time.toDuration
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
import org.hyperskill.app.profile.domain.model.isMobileContentTrialEnabled
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.profile.domain.repository.isFreemiumWrongSubmissionChargeLimitsEnabled
import org.hyperskill.app.purchases.domain.interactor.PurchaseInteractor
import org.hyperskill.app.subscriptions.domain.model.FreemiumChargeLimitsStrategy
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.model.SubscriptionType
import org.hyperskill.app.subscriptions.domain.model.isActive
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import org.hyperskill.app.subscriptions.domain.repository.areProblemsLimited

class SubscriptionsInteractor(
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val purchaseInteractor: PurchaseInteractor,
    private val authInteractor: AuthInteractor,
    logger: Logger
) {
    companion object {
        private const val LOG_TAG = "SubscriptionsInteractor"
        private const val SOLVING_FIRST_STEP_ADDITIONAL_LIMIT_VALUE = 10
    }

    private val logger: Logger = logger.withTag(LOG_TAG)

    private var refreshMobileOnlySubscriptionJob: Job? = null

    // Problems limits

    suspend fun chargeProblemsLimits(chargeStrategy: FreemiumChargeLimitsStrategy) {
        val isMobileContentTrialEnabled = currentProfileStateRepository
            .getState()
            .map { it.features.isMobileContentTrialEnabled }
            .getOrDefault(false)
        val areProblemsLimitEnabled = currentSubscriptionStateRepository.areProblemsLimited(
            isMobileContentTrialEnabled = isMobileContentTrialEnabled,
            canMakePayments = purchaseInteractor.canMakePayments().getOrDefault(false)
        )
        if (areProblemsLimitEnabled) {
            when (chargeStrategy) {
                FreemiumChargeLimitsStrategy.AFTER_WRONG_SUBMISSION -> chargeLimitsAfterWrongSubmission()
                FreemiumChargeLimitsStrategy.AFTER_CORRECT_SUBMISSION -> chargeLimitsAfterCorrectSubmission()
            }
        }
    }

    private suspend fun chargeLimitsAfterWrongSubmission() {
        if (currentProfileStateRepository.isFreemiumWrongSubmissionChargeLimitsEnabled()) {
            decreaseStepsLimitLeft()
        }
    }

    private suspend fun decreaseStepsLimitLeft() {
        currentSubscriptionStateRepository.updateState { subscription ->
            subscription.copy(stepsLimitLeft = subscription.stepsLimitLeft?.dec())
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



    // Refresh mobile only subscription

    suspend fun refreshSubscriptionOnExpirationIfNeeded(subscription: Subscription) {
        cancelSubscriptionRefresh()

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

        // ALTAPPS-1155: Add one minute to wait until the subscription is synced on the backend
        val delayDuration = subscriptionValidTill - nowByUTC + 1.toDuration(DurationUnit.MINUTES)
        logger.d { "Wait ${delayDuration.inWholeSeconds} seconds for subscription expiration to refresh it" }

        delay(delayDuration)
        if (isUserAuthorized()) {
            currentSubscriptionStateRepository
                .getState(forceUpdate = true)
                .onSuccess { freshSubscription ->
                    logger.d {
                        """Subscription successfully refreshed.
                           Type=${freshSubscription.type},status=${freshSubscription.status}
                        """.trimMargin()
                    }
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