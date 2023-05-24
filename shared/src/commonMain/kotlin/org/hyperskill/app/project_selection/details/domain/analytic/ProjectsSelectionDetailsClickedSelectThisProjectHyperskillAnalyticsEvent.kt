package org.hyperskill.app.project_selection.details.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents an analytic event for clicking on a select this project button in the projects selection details screen.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/projects/261?track=18",
 *     "action": "click",
 *     "part": "main",
 *     "target": "select_this_project"
 * }
 * ```
 *
 * @constructor Creates an analytic event for clicking on a select this project button in the projects selection details screen.
 *
 * @param projectId The project id where the event was triggered.
 * @param trackId The track id where the event was triggered.
 *
 * @see HyperskillAnalyticEvent
 */
class ProjectsSelectionDetailsClickedSelectThisProjectHyperskillAnalyticsEvent(
    projectId: Long,
    trackId: Long
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Projects.SelectProjectDetails(projectId, trackId),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.SELECT_THIS_PROJECT
)