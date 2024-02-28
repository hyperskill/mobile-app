package org.hyperskill.app.manage_subscription.presentation

import org.hyperskill.app.subscriptions.domain.model.isActive

internal val ManageSubscriptionFeature.State.Content.isSubscriptionManagementEnabled: Boolean
    get() = subscription.isActive && manageSubscriptionUrl != null