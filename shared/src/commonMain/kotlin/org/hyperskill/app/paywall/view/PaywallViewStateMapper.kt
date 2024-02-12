package org.hyperskill.app.paywall.view

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource.APP_STARTUP
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource.LOGIN
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource.PROBLEMS_LIMIT_MODAL
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource.PROFILE_SETTINGS
import org.hyperskill.app.paywall.presentation.PaywallFeature.State
import org.hyperskill.app.paywall.presentation.PaywallFeature.ViewState
import org.hyperskill.app.paywall.presentation.PaywallFeature.ViewStateContent

internal class PaywallViewStateMapper(
    private val resourceProvider: ResourceProvider
) {
    fun map(
        state: State,
        paywallTransitionSource: PaywallTransitionSource
    ): ViewState =
        ViewState(
            isToolbarVisible = when (paywallTransitionSource) {
                APP_STARTUP, LOGIN -> false
                PROFILE_SETTINGS, PROBLEMS_LIMIT_MODAL -> true
            },
            contentState = when (state) {
                State.Idle -> ViewStateContent.Idle
                State.Loading -> ViewStateContent.Loading
                State.Error -> ViewStateContent.Error
                is State.Content ->
                    if (state.isPurchaseSyncLoadingShowed) {
                        ViewStateContent.SubscriptionSyncLoading
                    } else {
                        ViewStateContent.Content(
                            buyButtonText = resourceProvider.getString(
                                SharedResources.strings.paywall_mobile_only_buy_btn,
                                state.formattedPrice
                            ),
                            isContinueWithLimitsButtonVisible = paywallTransitionSource != PROFILE_SETTINGS
                        )
                    }
            }
        )
}