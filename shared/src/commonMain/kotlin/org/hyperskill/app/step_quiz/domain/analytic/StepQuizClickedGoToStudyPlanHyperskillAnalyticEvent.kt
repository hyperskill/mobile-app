package org.hyperskill.app.step_quiz.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the "Go to Study Plan" button analytic event after unsupported quiz placeholder.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "unsupported_quiz_placeholder",
 *     "target": "go_to_study_plan"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class StepQuizClickedGoToStudyPlanHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.UNSUPPORTED_QUIZ_PLACEHOLDER,
    HyperskillAnalyticTarget.GO_TO_STUDY_PLAN
)