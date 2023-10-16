package org.hyperskill.app.auth.domain.analytic

import org.hyperskill.app.analytic.domain.model.apps_flyer.AppsFlyerAnalyticEvent

/**
 * Represents an analytic event when user signs up, e.g. account is created.
 *
 * @see AppsFlyerAnalyticEvent
 */
object AuthSignUpAppsFlyerAnalyticEvent : AppsFlyerAnalyticEvent(name = "af_complete_registration")