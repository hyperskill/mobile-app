package org.hyperskill.app.paywall.view

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource.LOGIN
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource.PROFILE_SETTINGS
import org.hyperskill.app.paywall.presentation.PaywallFeature.State
import org.hyperskill.app.paywall.presentation.PaywallFeature.ViewContentState
import org.hyperskill.app.paywall.presentation.PaywallFeature.ViewState

class PaywallViewStateMapper(
    private val resourceProvider: ResourceProvider
) {
    fun map(
        state: State,
        paywallTransitionSource: PaywallTransitionSource
    ): ViewState =
        ViewState(
            isToolbarVisible = paywallTransitionSource != LOGIN,
            contentState = when (state) {
                State.Idle -> ViewContentState.Idle
                State.Loading -> ViewContentState.Loading
                State.Error -> ViewContentState.Error
                is State.Content ->
                    ViewContentState.Content(
                        buyButtonText = resourceProvider.getString(
                            SharedResources.strings.paywall_mobile_only_buy_btn,
                            state.formattedPrice
                        ),
                        isContinueWithLimitsButtonVisible = paywallTransitionSource != PROFILE_SETTINGS
                    )
            }
        )
}