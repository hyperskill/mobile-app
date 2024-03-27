package org.hyperskill.app.step_theory_feedback.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a shown analytic event of the step theory feedback modal.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "shown",
 *     "part": "modal",
 *     "target": "step_theory_feedback_modal",
 *     "context": {
 *         "step_id": 1
 *     }
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class StepTheoryFeedbackModalShownHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute,
    stepId: Long
) : HyperskillAnalyticEvent(
    route = route,
    action = HyperskillAnalyticAction.SHOWN,
    part = HyperskillAnalyticPart.MODAL,
    target = HyperskillAnalyticTarget.STEP_THEORY_FEEDBACK_MODAL,
    context = mapOf(StepTheoryFeedbackAnalyticKeys.STEP_ID to stepId)
)