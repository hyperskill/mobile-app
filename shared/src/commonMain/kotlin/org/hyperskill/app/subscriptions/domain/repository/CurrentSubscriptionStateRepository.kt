package org.hyperskill.app.subscriptions.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.hyperskill.app.core.domain.repository.StateRepository
import org.hyperskill.app.core.domain.repository.StateWithSource
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.model.SubscriptionLimitType
import org.hyperskill.app.subscriptions.domain.model.getSubscriptionLimitType
import org.hyperskill.app.subscriptions.domain.model.orContentTrial

interface CurrentSubscriptionStateRepository : StateRepository<Subscription>

internal suspend fun CurrentSubscriptionStateRepository.areProblemsLimited(
    isMobileContentTrialEnabled: Boolean
): Boolean =
    getState(forceUpdate = false)
        .map {
            val subscriptionLimitType =
                it.getSubscriptionLimitType(isMobileContentTrialEnabled = isMobileContentTrialEnabled)
            subscriptionLimitType == SubscriptionLimitType.PROBLEMS
        }
        .getOrDefault(defaultValue = false)

internal suspend fun CurrentSubscriptionStateRepository.getState(
    isMobileContentTrialEnabled: Boolean,
    forceUpdate: Boolean = false
): Result<Subscription> =
    getState(forceUpdate = forceUpdate)
        .map { subscription ->
            subscription.orContentTrial(isMobileContentTrialEnabled)
        }

internal suspend fun CurrentSubscriptionStateRepository.getStateWithSource(
    isMobileContentTrialEnabled: Boolean,
    forceUpdate: Boolean = false
): Result<StateWithSource<Subscription>> =
    getStateWithSource(forceUpdate = forceUpdate)
        .map { subscriptionWithSource ->
            val subscription = subscriptionWithSource.state
            val mappedSubscription = subscription.orContentTrial(isMobileContentTrialEnabled)
            subscriptionWithSource.copy(state = mappedSubscription)
        }

internal fun CurrentSubscriptionStateRepository.changes(
    isMobileContentTrialEnabled: Boolean
): Flow<Subscription> =
    changes
        .distinctUntilChanged()
        .map { subscription ->
            subscription.orContentTrial(isMobileContentTrialEnabled)
        }