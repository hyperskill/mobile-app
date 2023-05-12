package org.hyperskill.app.project_selection.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

/**
 * Represents a view analytic event.
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
class ProjectsSelectionListViewedHyperskillAnalyticEvent(trackId: Long) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.ProjectsList(trackId),
    HyperskillAnalyticAction.VIEW
)