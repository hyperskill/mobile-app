package org.hyperskill.app.subscriptions.data.repository

import org.hyperskill.app.subscriptions.data.source.SubscriptionsRemoteDataSource
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.repository.SubscriptionsRepository

class SubscriptionsRepositoryImpl(
    private val subscriptionsRemoteDataSource: SubscriptionsRemoteDataSource
) : SubscriptionsRepository {
    override suspend fun syncSubscription(): Result<Subscription> =
        subscriptionsRemoteDataSource.syncSubscription()
}