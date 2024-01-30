package org.hyperskill.app.paywall.presentation

import org.hyperskill.app.core.flowredux.presentation.FlowView
import org.hyperskill.app.core.flowredux.presentation.ReduxFlowViewModel
import org.hyperskill.app.paywall.presentation.PaywallFeature.Action.ViewAction
import org.hyperskill.app.paywall.presentation.PaywallFeature.Message
import org.hyperskill.app.paywall.presentation.PaywallFeature.ViewState

class PaywallViewModel(
    reduxViewContainer: FlowView<ViewState, Message, ViewAction>
) : ReduxFlowViewModel<ViewState, Message, ViewAction>(reduxViewContainer) {

    fun onBuySubscriptionClick() {
        onNewMessage(Message.BuySubscriptionClicked)
    }

    fun onContinueWithLimitsClick() {
        onNewMessage(Message.ContinueWithLimitsClicked)
    }
}