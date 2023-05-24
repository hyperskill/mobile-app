package org.hyperskill.app.project_selection.list.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents an analytic event for clicking on a retry button in the projects-list.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/tracks/1/projects",
 *     "action": "click",
 *     "part": "main",
 *     "target": "retry",
 *     "context":
 *     {
 *         "id": 1
 *     }
 * }
 * ```
 *
 * @param trackId id of the track
 *
 * @see HyperskillAnalyticEvent
 */
class ProjectsSelectionListClickedRetryContentLoadingHyperskillAnalyticsEvent(
    trackId: Long
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Tracks.Projects(trackId),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.RETRY
)