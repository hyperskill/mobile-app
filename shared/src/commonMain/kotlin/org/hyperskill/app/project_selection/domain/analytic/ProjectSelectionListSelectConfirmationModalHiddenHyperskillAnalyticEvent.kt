package org.hyperskill.app.project_selection.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a shown analytic event of the stage implement unsupported bottom sheet.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/tracks/1/projects"
 *     "action": "hidden",
 *     "part": "modal",
 *     "target": "project_selection_modal"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class ProjectSelectionListSelectConfirmationModalHiddenHyperskillAnalyticEvent(
    trackId: Long
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.ProjectsList(trackId),
    HyperskillAnalyticAction.HIDDEN,
    HyperskillAnalyticPart.MODAL,
    HyperskillAnalyticTarget.PROJECT_SELECTION_MODAL
)