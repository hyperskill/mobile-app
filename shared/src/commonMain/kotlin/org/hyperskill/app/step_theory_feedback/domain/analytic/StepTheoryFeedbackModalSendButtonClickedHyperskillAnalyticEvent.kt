package org.hyperskill.app.step_theory_feedback.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a click on a "Send" button on the step theory feedback modal analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "step_theory_feedback_modal",
 *     "target": "send"
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
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.STEP_THEORY_FEEDBACK_MODAL,
    target = HyperskillAnalyticTarget.SEND,
    context = mapOf(
        StepTheoryFeedbackAnalyticKeys.STEP_ID to stepId,
        StepTheoryFeedbackAnalyticKeys.FEEDBACK_TEXT to feedback
    )
)