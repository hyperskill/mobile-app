package org.hyperskill.app.project_selection.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a click analytic event for clicking on the confirmation button on the project selection confirmation modal.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/tracks/1/projects"
 *     "action": "click",
 *     "part": "project_selection_modal",
 *     "target": "yes"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class ProjectSelectionListSelectConfirmationResultHyperskillAnalyticEvent(
    val trackId: Long,
    isConfirmed: Boolean
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.ProjectsList(trackId),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.PROJECT_SELECTION_MODAL,
    if (isConfirmed) HyperskillAnalyticTarget.YES else HyperskillAnalyticTarget.NO
)