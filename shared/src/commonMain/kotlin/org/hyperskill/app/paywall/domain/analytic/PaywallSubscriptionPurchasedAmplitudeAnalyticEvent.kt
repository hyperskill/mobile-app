package org.hyperskill.app.paywall.domain.analytic

import org.hyperskill.app.analytic.domain.model.amplitude.AmplitudeAnalyticEvent

/**
 * Represents an analytic event when user purchases a subscription.
 *
 * @see AmplitudeAnalyticEvent
 */
object PaywallSubscriptionPurchasedAmplitudeAnalyticEvent : AmplitudeAnalyticEvent(name = "buy_subscription")