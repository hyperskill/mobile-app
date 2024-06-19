package org.hyperskill.app.welcome_onboarding.root.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

/**
 * Represents a view analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/onboarding/finish",
 *     "action": "view"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
object WelcomeOnboardingFinishScreenViewedHSAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Onboarding.Finish,
    HyperskillAnalyticAction.VIEW
)