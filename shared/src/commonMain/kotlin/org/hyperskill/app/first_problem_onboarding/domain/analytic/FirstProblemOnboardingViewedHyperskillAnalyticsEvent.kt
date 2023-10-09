package org.hyperskill.app.first_problem_onboarding.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

/**
 * Represents a view analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/onboarding/first_problem",
 *     "action": "view"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
object FirstProblemOnboardingViewedHyperskillAnalyticsEvent :
    HyperskillAnalyticEvent(HyperskillAnalyticRoute.Onboarding.FirstProblem, HyperskillAnalyticAction.VIEW)