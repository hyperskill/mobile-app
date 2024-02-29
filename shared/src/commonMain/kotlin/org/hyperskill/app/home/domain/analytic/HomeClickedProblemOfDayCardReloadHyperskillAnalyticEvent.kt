package org.hyperskill.app.home.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the problem of the day card's reload button analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/home",
 *     "action": "click",
 *     "part": "problem_of_the_day_card",
 *     "target": "reload"
 *     "context":
 *     {
 *         "is_completed": false
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class HomeClickedProblemOfDayCardReloadHyperskillAnalyticEvent(
    val isCompleted: Boolean
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Home(),
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.PROBLEM_OF_THE_DAY_CARD,
    target = HyperskillAnalyticTarget.RELOAD,
    context = mapOf(HomeHyperskillAnalyticParams.PARAM_IS_COMPLETED to isCompleted)
)