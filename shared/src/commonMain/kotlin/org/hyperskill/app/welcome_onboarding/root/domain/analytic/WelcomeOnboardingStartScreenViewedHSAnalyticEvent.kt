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
 *     "route": "/onboarding",
 *     "action": "view"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
object WelcomeOnboardingStartScreenViewedHSAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Onboarding(),
    HyperskillAnalyticAction.VIEW
)