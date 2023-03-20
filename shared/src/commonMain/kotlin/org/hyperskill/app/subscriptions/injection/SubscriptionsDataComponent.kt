package org.hyperskill.app.subscriptions.injection

import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository

interface SubscriptionsDataComponent {
    val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository
}