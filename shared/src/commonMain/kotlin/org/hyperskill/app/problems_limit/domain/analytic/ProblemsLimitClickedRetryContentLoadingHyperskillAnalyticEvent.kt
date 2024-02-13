package org.hyperskill.app.problems_limit.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.problems_limit.domain.model.ProblemsLimitScreen

/**
 * Represents a click analytic event of the error state placeholder retry button.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/home",
 *     "action": "click",
 *     "part": "problems_limit_widget",
 *     "target": "retry"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class ProblemsLimitClickedRetryContentLoadingHyperskillAnalyticEvent(
    screen: ProblemsLimitScreen
) : HyperskillAnalyticEvent(
    screen.analyticRoute,
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.PROBLEMS_LIMIT_WIDGET,
    HyperskillAnalyticTarget.RETRY
)