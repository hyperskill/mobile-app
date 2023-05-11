package org.hyperskill.app.projects.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import ru.nobird.app.core.model.mapOfNotNull

/**
 * Represents an analytic event for clicking on a project in the projects-list.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/track/1/projects",
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
class ProjectsListClickedProjectHyperskillAnalyticsEvent(
    trackId: Long,
    private val projectId: Long
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.ProjectsList(trackId),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.PROJECTS_LIST,
    HyperskillAnalyticTarget.PROJECT
) {
    companion object {
        private const val ID = "id"
    }

    override val params: Map<String, Any>
        get() = super.params +
            mapOf(
                PARAM_CONTEXT to mapOfNotNull(
                    ID to projectId
                )
            )
}