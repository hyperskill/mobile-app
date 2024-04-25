package org.hyperskill.app.auth.domain.analytic

import org.hyperskill.app.analytic.domain.model.amplitude.AmplitudeAnalyticEvent

/**
 * Represents an analytic event when user signs up, e.g. account is created.
 *
 * @see AmplitudeAnalyticEvent
 */
object AuthSignUpAmplitudeAnalyticEvent : AmplitudeAnalyticEvent(name = "auth_sign_up")