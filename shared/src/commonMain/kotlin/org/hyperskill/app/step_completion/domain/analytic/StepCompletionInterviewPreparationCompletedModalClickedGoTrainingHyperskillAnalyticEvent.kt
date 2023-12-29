package org.hyperskill.app.step_completion.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the "Go to training" button analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/interview/1",
 *     "action": "click",
 *     "part": "interview_preparation_completed_modal",
 *     "target": "go_to_study_plan"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class StepCompletionInterviewPreparationCompletedModalClickedGoTrainingHyperskillAnalyticEvent(
    analyticRoute: HyperskillAnalyticRoute
) : HyperskillAnalyticEvent(
    analyticRoute,
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.INTERVIEW_PREPARATION_COMPLETED_MODAL,
    HyperskillAnalyticTarget.GO_TO_STUDY_PLAN
)