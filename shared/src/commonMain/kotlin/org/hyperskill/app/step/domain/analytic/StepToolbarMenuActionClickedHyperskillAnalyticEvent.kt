package org.hyperskill.app.step.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.step.domain.model.StepMenuSecondaryAction
import org.hyperskill.app.step.domain.model.StepRoute

/**
 * Represents a click on the step screen menu action.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/step/learn/1",
 *     "action": "click",
 *     "part": "head",
 *     "target": "share | skip | report | open_in_web",
 *     "context":  {
 *         "step_id": 1
 *     }
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class StepToolbarMenuActionClickedHyperskillAnalyticEvent(
    action: StepMenuSecondaryAction,
    stepRoute: StepRoute,
) : HyperskillAnalyticEvent(
    route = stepRoute.analyticRoute,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.HEAD,
    target = when (action) {
        StepMenuSecondaryAction.SHARE -> HyperskillAnalyticTarget.SHARE
        StepMenuSecondaryAction.REPORT -> HyperskillAnalyticTarget.REPORT
        StepMenuSecondaryAction.SKIP -> HyperskillAnalyticTarget.SKIP
        StepMenuSecondaryAction.OPEN_IN_WEB -> HyperskillAnalyticTarget.OPEN_IN_WEB
        StepMenuSecondaryAction.COMMENTS -> HyperskillAnalyticTarget.COMMENT
    },
    context = mapOf(StepAnalyticParams.PARAM_STEP_ID to stepRoute.stepId)
)