package org.hyperskill.app.problems_limit_info.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.problems_limit_info.domain.analytic.ProblemsLimitInfoModalAnalyticKeys.USER_INITIATED
import org.hyperskill.app.problems_limit_info.domain.model.ProblemsLimitInfoModalLaunchSource

/**
 * Represents click on the "Unlock unlimited problems" button in problems limit info modal analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "problems_limit_reached_modal",
 *     "target": "unlock_unlimited_problems",
 *     "context": {
 *         "user_initiated": true
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class ProblemsLimitInfoModalClickedUnlockUnlimitedProblemsHSAnalyticEvent(
    route: HyperskillAnalyticRoute,
    launchSource: ProblemsLimitInfoModalLaunchSource
) : HyperskillAnalyticEvent(
    route = route,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.PROBLEMS_LIMIT_REACHED_MODAL,
    target = HyperskillAnalyticTarget.UNLOCK_UNLIMITED_PROBLEMS,
    context = mapOf(USER_INITIATED to (launchSource == ProblemsLimitInfoModalLaunchSource.USER_INITIATED))
)