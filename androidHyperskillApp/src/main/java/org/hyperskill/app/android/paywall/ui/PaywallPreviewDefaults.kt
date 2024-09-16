package org.hyperskill.app.android.paywall.ui

import org.hyperskill.app.paywall.presentation.PaywallFeature

object PaywallPreviewDefaults {
    const val BUY_BUTTON_TEXT = "Start now"
    val subscriptionProducts = listOf(
        PaywallFeature.ViewStateContent.SubscriptionProduct(
            productId = "1",
            title = "Annual 100$",
            subtitle = "$8.33 / month",
            isBestValue = true,
            isSelected = true
        ),
        PaywallFeature.ViewStateContent.SubscriptionProduct(
            productId = "2",
            title = "Monthly",
            subtitle = "$12 / month",
            isBestValue = false,
            isSelected = false
        )
    )
}