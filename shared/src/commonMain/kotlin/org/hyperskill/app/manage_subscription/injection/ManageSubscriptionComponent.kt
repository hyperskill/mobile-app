package org.hyperskill.app.manage_subscription.injection

import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.Action
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.Message
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.State
import ru.nobird.app.presentation.redux.feature.Feature

interface ManageSubscriptionComponent {
    val manageSubscriptionFeature: Feature<State, Message, Action>
}