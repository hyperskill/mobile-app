package org.hyperskill.app.subscriptions.data.repository

import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.repository.SubscriptionsRepository
import org.hyperskill.app.subscriptions.remote.SubscriptionsRemoteDataSourceImpl

class SubscriptionsRepositoryImpl(
    private val subscriptionsRemoteDataSourceImpl: SubscriptionsRemoteDataSourceImpl
) : SubscriptionsRepository {
    override suspend fun syncSubscription(): Result<Subscription> =
        subscriptionsRemoteDataSourceImpl.syncSubscription()
}