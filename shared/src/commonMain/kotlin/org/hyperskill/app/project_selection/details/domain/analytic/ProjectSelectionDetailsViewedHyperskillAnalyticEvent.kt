package org.hyperskill.app.project_selection.details.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

/**
 * Represents a view analytic event for the projects selection details screen.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/projects/261?track=18",
 *     "action": "view"
 * }
 * ```
 *
 * @constructor Creates a view analytic event for the projects selection details screen.
 *
 * @param projectId The project id where the event was triggered.
 * @param trackId The track id where the event was triggered.
 *
 * @see HyperskillAnalyticEvent
 */
class ProjectSelectionDetailsViewedHyperskillAnalyticEvent(
    projectId: Long,
    trackId: Long
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Projects.SelectProjectDetails(projectId, trackId),
    HyperskillAnalyticAction.VIEW
)