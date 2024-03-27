package org.hyperskill.app.step_theory_feedback.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a click on a "Send" button on the theory feedback modal analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "shown",
 *     "part": "modal",
 *     "target": "theory_feedback_modal"
 *     "context": {
 *         "step_id": 1,
 *         "feedback_text": "some feedback"
 *     }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class StepTheoryFeedbackModalSendButtonClickedHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute,
    stepId: Long,
    feedback: String
) : HyperskillAnalyticEvent(
    route = route,
    action = HyperskillAnalyticAction.SHOWN,
    part = HyperskillAnalyticPart.MODAL,
    target = HyperskillAnalyticTarget.SEND_THEORY_FEEDBACK,
    context = mapOf(
        StepTheoryFeedbackAnalyticKeys.STEP_ID to stepId,
        StepTheoryFeedbackAnalyticKeys.FEEDBACK_TEXT to feedback
    )
)