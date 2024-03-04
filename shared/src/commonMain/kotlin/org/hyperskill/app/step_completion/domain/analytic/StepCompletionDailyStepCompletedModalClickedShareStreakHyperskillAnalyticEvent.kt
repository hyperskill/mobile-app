package org.hyperskill.app.step_completion.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the "Share your streak" button analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "daily_step_completed_modal",
 *     "target": "share_your_streak",
 *     "context":
 *     {
 *         "streak": 1
 *     }
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class StepCompletionDailyStepCompletedModalClickedShareStreakHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute,
    val streak: Int
) : HyperskillAnalyticEvent(
    route = route,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.DAILY_STEP_COMPLETED_MODAL,
    target = HyperskillAnalyticTarget.SHARE_YOUR_STREAK,
    context = mapOf(StepCompletionHyperskillAnalyticParams.PARAM_STREAK to streak)
)