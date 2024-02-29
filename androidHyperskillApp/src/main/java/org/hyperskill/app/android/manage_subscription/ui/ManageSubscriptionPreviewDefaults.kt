package org.hyperskill.app.android.manage_subscription.ui

import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature

object ManageSubscriptionPreviewDefaults {
    const val VALID_UNTIL_FORMATTED = "Valid until January 27, 2024, 02:00"

    val ActiveSubscriptionContent: ManageSubscriptionFeature.ViewState.Content
        get() = ManageSubscriptionFeature.ViewState.Content(
            validUntilFormatted = VALID_UNTIL_FORMATTED,
            buttonText = "Manage subscription"
        )

    val ExpiredSubscriptionContent: ManageSubscriptionFeature.ViewState.Content
        get() = ManageSubscriptionFeature.ViewState.Content(
            validUntilFormatted = VALID_UNTIL_FORMATTED,
            buttonText = "Renew subscription"
        )
}