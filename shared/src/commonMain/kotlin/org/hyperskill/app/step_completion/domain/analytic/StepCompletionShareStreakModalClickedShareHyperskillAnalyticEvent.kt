package org.hyperskill.app.step_completion.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the "Share" button analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "share_streak_modal",
 *     "target": "share",
 *     "context":
 *     {
 *         "streak": 1
 *     }
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class StepCompletionShareStreakModalClickedShareHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute,
    val streak: Int
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.SHARE_STREAK_MODAL,
    HyperskillAnalyticTarget.SHARE
) {
    override val params: Map<String, Any>
        get() = super.params + mapOf(
            PARAM_CONTEXT to mapOf(StepCompletionHyperskillAnalyticParams.PARAM_STREAK to streak)
        )
}