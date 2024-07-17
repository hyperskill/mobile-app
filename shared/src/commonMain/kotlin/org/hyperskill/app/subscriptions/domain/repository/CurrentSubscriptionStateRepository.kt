package org.hyperskill.app.subscriptions.domain.repository

import org.hyperskill.app.core.domain.repository.StateRepository
import org.hyperskill.app.core.domain.repository.StateWithSource
import org.hyperskill.app.subscriptions.domain.model.ProblemsLimitType
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.model.SubscriptionType
import org.hyperskill.app.subscriptions.domain.model.getProblemsLimitType

interface CurrentSubscriptionStateRepository : StateRepository<Subscription>

internal suspend fun CurrentSubscriptionStateRepository.isDailyProblemsEnabled(
    isMobileContentTrialEnabled: Boolean
): Boolean =
    getState(forceUpdate = false)
        .map {
            val problemsLimitType = it.getProblemsLimitType(isMobileContentTrialEnabled = isMobileContentTrialEnabled)
            problemsLimitType == ProblemsLimitType.DAILY
        }
        .getOrDefault(defaultValue = false)

internal suspend fun CurrentSubscriptionStateRepository.getState(
    isMobileContentTrialEnabled: Boolean,
    forceUpdate: Boolean = false
): Result<Subscription> =
    getState(forceUpdate = forceUpdate)
        .map { subscription ->
            mapSubscription(subscription, isMobileContentTrialEnabled)
        }

internal suspend fun CurrentSubscriptionStateRepository.getStateWithSource(
    isMobileContentTrialEnabled: Boolean,
    forceUpdate: Boolean = false
): Result<StateWithSource<Subscription>> =
    getStateWithSource(forceUpdate = forceUpdate)
        .map { subscriptionWithSource ->
            val subscription = subscriptionWithSource.state
            val mappedSubscription = mapSubscription(subscription, isMobileContentTrialEnabled)
            subscriptionWithSource.copy(state = mappedSubscription)
        }

private fun mapSubscription(
    subscription: Subscription,
    isMobileContentTrialEnabled: Boolean
): Subscription =
    if (subscription.type == SubscriptionType.FREEMIUM && isMobileContentTrialEnabled) {
        Subscription(type = SubscriptionType.MOBILE_CONTENT_TRIAL)
    } else {
        subscription
    }