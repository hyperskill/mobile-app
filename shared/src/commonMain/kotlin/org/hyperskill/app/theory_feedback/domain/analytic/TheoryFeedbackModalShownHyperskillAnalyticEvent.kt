package org.hyperskill.app.theory_feedback.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a shown analytic event of the theory feedback modal.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "shown",
 *     "part": "modal",
 *     "target": "theory_feedback_modal",
 *     "context": {
 *         "step_id": 1
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
internal class TheoryFeedbackModalShownHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute,
    stepId: Long
) : HyperskillAnalyticEvent(
    route = route,
    action = HyperskillAnalyticAction.SHOWN,
    part = HyperskillAnalyticPart.MODAL,
    target = HyperskillAnalyticTarget.THEORY_FEEDBACK_MODAL,
    context = mapOf(TheoryFeedbackAnalyticKeys.STEP_ID to stepId)
)