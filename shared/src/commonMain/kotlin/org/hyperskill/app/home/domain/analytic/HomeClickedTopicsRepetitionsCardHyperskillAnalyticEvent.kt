package org.hyperskill.app.home.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the topics repetitions card analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/home",
 *     "action": "click",
 *     "part": "topics_repetitions_card",
 *     "target": "continue",
 *     "context":
 *     {
 *         "is_completed": false
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class HomeClickedTopicsRepetitionsCardHyperskillAnalyticEvent(
    val isCompleted: Boolean
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Home(),
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.TOPICS_REPETITIONS_CARD,
    target = HyperskillAnalyticTarget.CONTINUE,
    context = mapOf(HomeHyperskillAnalyticParams.PARAM_IS_COMPLETED to isCompleted)
)