package org.hyperskill.app.subscriptions.domain.repository

import org.hyperskill.app.subscriptions.domain.model.Subscription

interface SubscriptionsRepository {
    suspend fun syncSubscription(): Result<Subscription>
}