package org.hyperskill.app.subscriptions.domain.repository

import org.hyperskill.app.core.domain.repository.StateRepository
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.model.areProblemsLimited

interface CurrentSubscriptionStateRepository : StateRepository<Subscription>

internal suspend fun CurrentSubscriptionStateRepository.areProblemsLimited(): Boolean =
    getState(forceUpdate = false)
        .map { it.areProblemsLimited }
        .getOrDefault(defaultValue = false)