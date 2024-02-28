package org.hyperskill.app.subscriptions.injection

import org.hyperskill.app.subscriptions.domain.interactor.SubscriptionsInteractor
import org.hyperskill.app.subscriptions.domain.repository.SubscriptionsRepository

interface SubscriptionsDataComponent {
    val subscriptionsRepository: SubscriptionsRepository
    val subscriptionsInteractor: SubscriptionsInteractor
}