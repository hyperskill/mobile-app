package org.hyperskill.app.subscriptions.data.repository

import org.hyperskill.app.core.data.repository.BaseStateRepository
import org.hyperskill.app.subscriptions.data.source.CurrentSubscriptionStateHolder
import org.hyperskill.app.subscriptions.data.source.SubscriptionsRemoteDataSource
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository

internal class CurrentSubscriptionStateRepositoryImpl(
    private val subscriptionsRemoteDataSource: SubscriptionsRemoteDataSource,
    override val stateHolder: CurrentSubscriptionStateHolder
) : CurrentSubscriptionStateRepository, BaseStateRepository<Subscription>() {
    override suspend fun loadState(): Result<Subscription> =
        subscriptionsRemoteDataSource.getCurrentSubscription()
}