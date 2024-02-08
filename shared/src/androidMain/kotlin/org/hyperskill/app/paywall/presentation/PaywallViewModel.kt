package org.hyperskill.app.paywall.presentation

import android.app.Activity
import org.hyperskill.app.core.flowredux.presentation.FlowView
import org.hyperskill.app.core.flowredux.presentation.ReduxFlowViewModel
import org.hyperskill.app.paywall.presentation.PaywallFeature.Action.ViewAction
import org.hyperskill.app.paywall.presentation.PaywallFeature.Message
import org.hyperskill.app.paywall.presentation.PaywallFeature.ViewState
import org.hyperskill.app.purchases.domain.model.AndroidPurchaseParams

class PaywallViewModel(
    reduxViewContainer: FlowView<ViewState, Message, ViewAction>
) : ReduxFlowViewModel<ViewState, Message, ViewAction>(reduxViewContainer) {

    init {
        onNewMessage(Message.Initialize)
    }

    fun onBuySubscriptionClick(activity: Activity) {
        onNewMessage(
            Message.BuySubscriptionClicked(
                AndroidPurchaseParams(activity)
            )
        )
    }

    fun onContinueWithLimitsClick() {
        onNewMessage(Message.ContinueWithLimitsClicked)
    }

    fun onRetryLoadingClicked() {
        onNewMessage(Message.RetryContentLoading)
    }
}