package org.hyperskill.app.first_problem_onboarding.domain.analytics

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the "Start/Keep learning" button analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/onboarding/first_problem",
 *     "action": "click",
 *     "part": "main",
 *     "target": "learning_action"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
object FirstProblemOnboardingClickedLearningActionHyperskillAnalyticsEvent : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Onboarding.FirstProblem,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.MAIN,
    target = HyperskillAnalyticTarget.LEARNING_ACTION
)