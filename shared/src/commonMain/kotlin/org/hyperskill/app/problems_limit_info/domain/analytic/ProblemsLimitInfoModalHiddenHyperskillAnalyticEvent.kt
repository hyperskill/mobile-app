package org.hyperskill.app.problems_limit_info.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.problems_limit_info.domain.analytic.ProblemsLimitInfoModalAnalyticKeys.USER_INITIATED
import org.hyperskill.app.problems_limit_info.domain.model.ProblemsLimitInfoModalContext

/**
 * Represents a hidden analytic event of the problems limit info modal.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "hidden",
 *     "part": "problems_limit_reached_modal",
 *     "target": "close",
 *     "context": {
 *         "user_initiated": true
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class ProblemsLimitInfoModalHiddenHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute,
    context: ProblemsLimitInfoModalContext
) : HyperskillAnalyticEvent(
    route = route,
    action = HyperskillAnalyticAction.HIDDEN,
    part = HyperskillAnalyticPart.PROBLEMS_LIMIT_REACHED_MODAL,
    target = HyperskillAnalyticTarget.CLOSE,
    context = mapOf(USER_INITIATED to (context == ProblemsLimitInfoModalContext.USER_INITIATED))
)