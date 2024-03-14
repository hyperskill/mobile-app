package org.hyperskill.paywall

import kotlin.test.Test
import kotlin.test.assertTrue
import org.hyperskill.ResourceProviderStub
import org.hyperskill.app.paywall.domain.analytic.PaywallSubscriptionPurchasedAppsFlyerAnalyticEvent
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.paywall.presentation.PaywallFeature
import org.hyperskill.app.paywall.presentation.PaywallFeature.InternalAction
import org.hyperskill.app.paywall.presentation.PaywallFeature.InternalMessage
import org.hyperskill.app.paywall.presentation.PaywallReducer
import org.hyperskill.app.purchases.domain.model.PurchaseResult

class PaywallTest {
    private val reducer = PaywallReducer(
        paywallTransitionSource = PaywallTransitionSource.LOGIN,
        resourceProvider = ResourceProviderStub()
    )

    @Test
    fun `Success subscription purchase should log analytic event`() {
        val (_, actions) = reducer.reduce(
            PaywallFeature.State.Content(formattedPrice = "", isPurchaseSyncLoadingShowed = false),
            InternalMessage.MobileOnlySubscriptionPurchaseSuccess(
                PurchaseResult.Succeed(orderId = null, productIds = emptyList())
            )
        )
        assertTrue {
            actions.any {
                it == InternalAction.LogAnalyticEvent(PaywallSubscriptionPurchasedAppsFlyerAnalyticEvent)
            }
        }
    }
}