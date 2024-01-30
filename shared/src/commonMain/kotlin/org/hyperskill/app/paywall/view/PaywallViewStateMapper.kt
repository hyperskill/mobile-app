package org.hyperskill.app.paywall.view

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.paywall.presentation.PaywallFeature.State
import org.hyperskill.app.paywall.presentation.PaywallFeature.ViewState

class PaywallViewStateMapper(
    private val resourceProvider: ResourceProvider
) {
    fun map(state: State): ViewState =
        when (state) {
            State.Idle -> ViewState.Idle
            State.Loading -> ViewState.Error
            State.Error -> ViewState.Error
            is State.Content ->
                ViewState.Content(
                    buyButtonText = resourceProvider.getString(
                        SharedResources.strings.paywall_mobile_only_buy_btn,
                        state.formattedPrice
                    )
                )
        }
}