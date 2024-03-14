package org.hyperskill.app.paywall.domain.analytic

import org.hyperskill.app.analytic.domain.model.apps_flyer.AppsFlyerAnalyticEvent

/**
 * Represents an analytic event when user purchases a subscription.
 */
object PaywallSubscriptionPurchasedAppsFlyerAnalyticEvent : AppsFlyerAnalyticEvent(name = "af_subscribe")