package org.hyperskill.app.step_quiz.domain.analytic.daily_limit_reached_modal

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a hidden analytic event of the daily limit reached modal.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "hidden",
 *     "part": "daily_limit_reached_modal",
 *     "target": "close"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class DailyProblemsLimitReachedModalHiddenHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.HIDDEN,
    HyperskillAnalyticPart.DAILY_LIMIT_REACHED_MODAL,
    HyperskillAnalyticTarget.CLOSE
)