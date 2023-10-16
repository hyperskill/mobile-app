package org.hyperskill.app.auth.domain.analytic

import org.hyperskill.app.analytic.domain.model.apps_flyer.AppsFlyerAnalyticEvent

/**
 * Represents an analytic event when user signs in, e.g. logged in into existed account.
 *
 * @see AppsFlyerAnalyticEvent
 */
object AuthSignInAppsFlyerAnalyticEvent : AppsFlyerAnalyticEvent(name = "af_login")