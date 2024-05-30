package org.hyperskill.app.step.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.step.domain.model.StepMenuAction
import org.hyperskill.app.step.domain.model.StepRoute

/**
 * Represents click on the home screen quick action analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/step/learn/1",
 *     "action": "click",
 *     "part": "main",
 *     "target": "share | skip | report | open_in_web",
 *     "context":  {
 *         "step_id": 1
 *     }
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class StepToolbarActionClickedHyperskillAnalyticEvent(
    action: StepMenuAction,
    stepRoute: StepRoute,
) : HyperskillAnalyticEvent(
    route = stepRoute.analyticRoute,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.MAIN,
    target = when (action) {
        StepMenuAction.SHARE -> HyperskillAnalyticTarget.SHARE
        StepMenuAction.REPORT -> HyperskillAnalyticTarget.REPORT
        StepMenuAction.SKIP -> HyperskillAnalyticTarget.SKIP
        StepMenuAction.OPEN_IN_WEB -> HyperskillAnalyticTarget.OPEN_IN_WEB
    },
    context = mapOf(STEP_ID_KEY to stepRoute.stepId)
) {
    companion object {
        private const val STEP_ID_KEY = "step_id"
    }
}