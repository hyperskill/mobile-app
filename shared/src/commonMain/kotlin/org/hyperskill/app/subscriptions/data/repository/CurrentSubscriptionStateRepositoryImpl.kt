package org.hyperskill.app.subscriptions.data.repository

import org.hyperskill.app.core.data.repository.InMemoryStateRepository
import org.hyperskill.app.subscriptions.data.source.SubscriptionsRemoteDataSource
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository

class CurrentSubscriptionStateRepositoryImpl(
    private val subscriptionsRemoteDataSource: SubscriptionsRemoteDataSource
) :  CurrentSubscriptionStateRepository, InMemoryStateRepository<Subscription>() {
    override suspend fun loadState(): Result<Subscription> =
        subscriptionsRemoteDataSource.getCurrentSubscription()
}