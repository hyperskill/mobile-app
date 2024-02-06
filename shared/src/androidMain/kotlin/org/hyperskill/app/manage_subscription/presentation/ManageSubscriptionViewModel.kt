package org.hyperskill.app.manage_subscription.presentation

import org.hyperskill.app.core.flowredux.presentation.FlowView
import org.hyperskill.app.core.flowredux.presentation.ReduxFlowViewModel
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.Action.ViewAction
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.Message
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.State

class ManageSubscriptionViewModel(
    viewContainer: FlowView<State, Message, ViewAction>
) : ReduxFlowViewModel<State, Message, ViewAction>(viewContainer) {
    init {
        onNewMessage(Message.Initialize)
    }
}