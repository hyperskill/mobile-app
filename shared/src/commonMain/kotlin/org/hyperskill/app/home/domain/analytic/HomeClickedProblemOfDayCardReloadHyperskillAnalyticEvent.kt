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
    HyperskillAnalyticRoute.Home(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.PROBLEM_OF_THE_DAY_CARD,
    HyperskillAnalyticTarget.RELOAD
) {
    override val params: Map<String, Any>
        get() = super.params + mapOf(
            PARAM_CONTEXT to
                mapOf(HomeHyperskillAnalyticParams.PARAM_IS_COMPLETED to isCompleted)
        )
}