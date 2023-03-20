package org.hyperskill.app.subscriptions.data.source

import org.hyperskill.app.subscriptions.domain.model.Subscription

interface SubscriptionsRemoteDataSource {
    suspend fun getCurrentSubscription(): Result<Subscription>
}