package org.hyperskill.app.subscriptions.data.source

import org.hyperskill.app.core.domain.repository.StateHolder
import org.hyperskill.app.subscriptions.domain.model.Subscription

internal interface CurrentSubscriptionStateHolder : StateHolder<Subscription>