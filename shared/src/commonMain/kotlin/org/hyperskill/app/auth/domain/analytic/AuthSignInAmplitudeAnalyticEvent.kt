package org.hyperskill.app.auth.domain.analytic

import org.hyperskill.app.analytic.domain.model.amplitude.AmplitudeAnalyticEvent

/**
 * Represents an analytic event when user signs in, e.g. logged in into existed account.
 *
 * @see AmplitudeAnalyticEvent
 */
object AuthSignInAmplitudeAnalyticEvent : AmplitudeAnalyticEvent(name = "auth_sign_in")