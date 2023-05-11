package org.hyperskill.app.projects.domain.analytic

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
 *     "route": "/track/1/projects",
 *     "action": "click",
 *     "part": "main",
 *     "target": "refresh"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class ProjectsListClickedPullToRefreshHyperskillAnalyticEvent(
    trackId: Long
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.ProjectsList(trackId),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.REFRESH
)