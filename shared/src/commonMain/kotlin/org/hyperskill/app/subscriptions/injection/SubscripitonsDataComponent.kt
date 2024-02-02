package org.hyperskill.app.subscriptions.injection

import org.hyperskill.app.subscriptions.domain.repository.SubscriptionsRepository

interface SubscripitonsDataComponent {
    val subscriptionsRepository: SubscriptionsRepository
}