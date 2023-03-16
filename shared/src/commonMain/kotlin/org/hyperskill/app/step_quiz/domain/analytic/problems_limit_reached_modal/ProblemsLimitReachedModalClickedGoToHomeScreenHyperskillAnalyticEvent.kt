package org.hyperskill.app.step_quiz.domain.analytic.problems_limit_reached_modal

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the "Go to homescreen" button in problems limit reached modal analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "problems_limit_reached_modal",
 *     "target": "go_to_home_screen"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class ProblemsLimitReachedModalClickedGoToHomeScreenHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.PROBLEMS_LIMIT_REACHED_MODAL,
    HyperskillAnalyticTarget.GO_TO_HOME_SCREEN
)