package org.hyperskill.app.subscriptions.domain.repository

import org.hyperskill.app.core.domain.repository.StateRepository
import org.hyperskill.app.subscriptions.domain.model.Subscription

interface CurrentSubscriptionStateRepository : StateRepository<Subscription>