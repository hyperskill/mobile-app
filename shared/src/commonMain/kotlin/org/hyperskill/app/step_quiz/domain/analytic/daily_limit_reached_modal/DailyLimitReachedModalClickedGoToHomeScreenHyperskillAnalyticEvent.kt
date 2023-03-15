package org.hyperskill.app.step_quiz.domain.analytic.daily_limit_reached_modal

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the "Go to homescreen" button in daily limit reached modal analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "daily_limit_reached_modal",
 *     "target": "go_to_home_screen"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class DailyLimitReachedModalClickedGoToHomeScreenHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.TOPIC_COMPLETED_MODAL,
    HyperskillAnalyticTarget.GO_TO_HOME_SCREEN
)