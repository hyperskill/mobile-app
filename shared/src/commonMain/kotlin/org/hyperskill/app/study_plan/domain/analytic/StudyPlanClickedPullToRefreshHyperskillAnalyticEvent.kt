package org.hyperskill.app.study_plan.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a PullToRefresh analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/study-plan"",
 *     "action": "click",
 *     "part": "main",
 *     "target": "refresh"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class StudyPlanClickedPullToRefreshHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.StudyPlan(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.REFRESH
)