package org.hyperskill.app.first_problem_onboarding.domain.analytic

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
 *     "target": "start_learning/keep_learning"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class FirstProblemOnboardingClickedLearningActionHyperskillAnalyticEvent(
    target: HyperskillAnalyticTarget
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Onboarding.FirstProblem,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.MAIN,
    target = target
)