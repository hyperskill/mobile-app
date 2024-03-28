package org.hyperskill.app.step_theory_feedback.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a hidden analytic event of the step theory feedback modal.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "hidden",
 *     "part": "step_theory_feedback_modal",
 *     "target": "close",
 *     "context": {
 *         "step_id": 1
 *     }
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class StepTheoryFeedbackModalHiddenHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute,
    stepId: Long
) : HyperskillAnalyticEvent(
    route = route,
    action = HyperskillAnalyticAction.HIDDEN,
    part = HyperskillAnalyticPart.STEP_THEORY_FEEDBACK_MODAL,
    target = HyperskillAnalyticTarget.CLOSE,
    context = mapOf(StepTheoryFeedbackAnalyticKeys.STEP_ID to stepId)
)