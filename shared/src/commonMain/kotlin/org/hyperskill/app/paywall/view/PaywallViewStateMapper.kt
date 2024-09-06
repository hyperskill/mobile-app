package org.hyperskill.app.paywall.view

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.domain.platform.PlatformType
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource.APP_BECOMES_ACTIVE
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource.MANAGE_SUBSCRIPTION
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource.PROBLEMS_LIMIT_MODAL
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource.PROFILE_SETTINGS
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource.STUDY_PLAN
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource.TOPIC_COMPLETED_MODAL
import org.hyperskill.app.paywall.presentation.PaywallFeature.State
import org.hyperskill.app.paywall.presentation.PaywallFeature.ViewState
import org.hyperskill.app.paywall.presentation.PaywallFeature.ViewStateContent
import org.hyperskill.app.purchases.domain.model.SubscriptionPeriod
import org.hyperskill.app.purchases.domain.model.SubscriptionProduct

internal class PaywallViewStateMapper(
    private val resourceProvider: ResourceProvider,
    private val platformType: PlatformType
) {
    fun map(
        state: State,
        paywallTransitionSource: PaywallTransitionSource
    ): ViewState =
        ViewState(
            isToolbarVisible = when (paywallTransitionSource) {
                PROFILE_SETTINGS, MANAGE_SUBSCRIPTION -> true
                APP_BECOMES_ACTIVE,
                PROBLEMS_LIMIT_MODAL,
                TOPIC_COMPLETED_MODAL,
                STUDY_PLAN -> false
            },
            contentState = when (state) {
                State.Idle -> ViewStateContent.Idle
                State.Loading -> ViewStateContent.Loading
                State.Error -> ViewStateContent.Error
                is State.Content ->
                    if (state.isPurchaseSyncLoadingShowed) {
                        ViewStateContent.SubscriptionSyncLoading
                    } else {
                        getContentViewState(state)
                    }
            }
        )

    private fun getContentViewState(state: State.Content): ViewStateContent.Content =
        ViewStateContent.Content(
            buyButtonText = resourceProvider.getString(SharedResources.strings.paywall_subscription_start_btn),
            subscriptionProducts = state.subscriptionProducts.mapIndexed { i, product ->
                mapSubscriptionProductToSubscriptionOption(
                    index = i,
                    product = product,
                    isSelected = product.id == state.selectedProductId
                )
            },
            trialText = null/*if (platformType == PlatformType.IOS && state.isTrialEligible) {
                resourceProvider.getString(
                    SharedResources.strings.paywall_ios_mobile_only_trial_description,
                    state.formattedPrice
                )
            } else {
                null
            }*/
        )

    private fun mapSubscriptionProductToSubscriptionOption(
        index: Int,
        product: SubscriptionProduct,
        isSelected: Boolean
    ): ViewStateContent.SubscriptionProduct =
        ViewStateContent.SubscriptionProduct(
            productId = product.id,
            title = when (product.period) {
                SubscriptionPeriod.MONTH ->
                    resourceProvider.getString(SharedResources.strings.paywall_subscription_duration_monthly)
                SubscriptionPeriod.YEAR ->
                    resourceProvider.getString(
                        SharedResources.strings.paywall_subscription_duration_annual,
                        product.formattedPrice
                    )
            },
            subtitle = resourceProvider.getString(
                SharedResources.strings.paywall_subscription_month_price,
                product.formattedPricePerMonth
            ),
            isBestValue = index == 0,
            isSelected = isSelected
        )

    /*private fun getBuyButtonText(state: State.Content): String =
        when (platformType) {
            PlatformType.IOS ->
                if (state.isTrialEligible) {
                    resourceProvider.getString(SharedResources.strings.paywall_ios_mobile_only_trial_buy_btn)
                } else {
                    resourceProvider.getString(SharedResources.strings.paywall_ios_mobile_only_buy_btn)
                }
            PlatformType.ANDROID ->
                resourceProvider.getString(SharedResources.strings.paywall_subscription_start_btn)
        }*/
}