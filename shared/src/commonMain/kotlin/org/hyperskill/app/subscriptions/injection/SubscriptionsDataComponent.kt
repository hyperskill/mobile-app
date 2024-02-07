package org.hyperskill.app.subscriptions.injection

import org.hyperskill.app.subscriptions.domain.repository.SubscriptionsRepository

interface SubscriptionsDataComponent {
    val subscriptionsRepository: SubscriptionsRepository
}