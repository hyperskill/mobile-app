package org.hyperskill.app.problems_limit.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a click on the problems limit navigation bar button item analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "head",
 *     "target": "problems_limit"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class StepQuizToolbarLimitClickedHyperskillAnalyticEvent(
    analyticRoute: HyperskillAnalyticRoute
) : HyperskillAnalyticEvent(
    route = analyticRoute,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.HEAD,
    target = HyperskillAnalyticTarget.PROBLEMS_LIMIT
)