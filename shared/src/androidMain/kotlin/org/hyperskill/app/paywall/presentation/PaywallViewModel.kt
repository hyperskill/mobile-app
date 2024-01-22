package org.hyperskill.app.paywall.presentation

import org.hyperskill.app.core.flowredux.presentation.FlowView
import org.hyperskill.app.core.flowredux.presentation.ReduxFlowViewModel
import org.hyperskill.app.paywall.presentation.PaywallFeature.Action.ViewAction
import org.hyperskill.app.paywall.presentation.PaywallFeature.Message
import org.hyperskill.app.paywall.presentation.PaywallFeature.State

class PaywallViewModel(
    reduxViewContainer: FlowView<State, Message, ViewAction>
) : ReduxFlowViewModel<State, Message, ViewAction>(reduxViewContainer)