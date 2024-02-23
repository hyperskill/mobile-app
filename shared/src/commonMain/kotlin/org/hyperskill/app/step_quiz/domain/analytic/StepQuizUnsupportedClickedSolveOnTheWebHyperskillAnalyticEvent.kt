package org.hyperskill.app.step_quiz.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the "Solve on the Web version" button analytic event after unsupported quiz placeholder.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "unsupported_quiz_placeholder",
 *     "target": "solve_on_the_web_version"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class StepQuizUnsupportedClickedSolveOnTheWebHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.UNSUPPORTED_QUIZ_PLACEHOLDER,
    HyperskillAnalyticTarget.SOLVE_ON_THE_WEB_VERSION
)