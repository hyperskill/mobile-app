package org.hyperskill.app.subscriptions.domain.repository

import org.hyperskill.app.core.domain.repository.StateRepository
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.model.SubscriptionLimitType
import org.hyperskill.app.subscriptions.domain.model.getSubscriptionLimitType

interface CurrentSubscriptionStateRepository : StateRepository<Subscription>

internal suspend fun CurrentSubscriptionStateRepository.areProblemsLimited(
    isMobileContentTrialEnabled: Boolean,
    canMakePayments: Boolean
): Boolean =
    getState(forceUpdate = false)
        .map {
            val subscriptionLimitType =
                it.getSubscriptionLimitType(
                    isMobileContentTrialEnabled = isMobileContentTrialEnabled,
                    canMakePayments = canMakePayments
                )
            subscriptionLimitType == SubscriptionLimitType.PROBLEMS
        }
        .getOrDefault(defaultValue = false)