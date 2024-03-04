package org.hyperskill.app.project_selection.list.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import ru.nobird.app.core.model.mapOfNotNull

/**
 * Represents an analytic event for clicking on a project in the projects selection list screen.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/tracks/1/projects",
 *     "action": "click",
 *     "part": "projects_list",
 *     "target": "project",
 *     "context":
 *     {
 *         "id": 1
 *     }
 * }
 * ```
 *
 * @param trackId id of the track
 * @param projectId is of the project
 *
 * @see HyperskillAnalyticEvent
 */
class ProjectSelectionListClickedProjectHyperskillAnalyticEvent(
    val trackId: Long,
    val projectId: Long
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Tracks.Projects(trackId),
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.PROJECTS_LIST,
    target = HyperskillAnalyticTarget.PROJECT,
    context = mapOfNotNull(ID to projectId)
) {
    companion object {
        private const val ID = "id"
    }
}