package org.hyperskill.app.step_completion.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a shown analytic event of the interview preparation completed modal.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/interview/1",
 *     "action": "shown",
 *     "part": "modal",
 *     "target": "interview_preparation_completed_modal"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class StepCompletionInterviewPreparationCompletedModalShownHyperskillAnalyticEvent(
    analyticRoute: HyperskillAnalyticRoute
) : HyperskillAnalyticEvent(
    analyticRoute,
    HyperskillAnalyticAction.SHOWN,
    HyperskillAnalyticPart.MODAL,
    HyperskillAnalyticTarget.INTERVIEW_PREPARATION_COMPLETED_MODAL
)