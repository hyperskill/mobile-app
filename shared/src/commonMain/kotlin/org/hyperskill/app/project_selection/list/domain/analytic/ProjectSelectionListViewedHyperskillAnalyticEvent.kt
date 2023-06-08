package org.hyperskill.app.project_selection.list.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

/**
 * Represents a view analytic event for the project selection list screen.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/tracks/1/projects",
 *     "action": "view"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class ProjectSelectionListViewedHyperskillAnalyticEvent(trackId: Long) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Tracks.Projects(trackId),
    HyperskillAnalyticAction.VIEW
)