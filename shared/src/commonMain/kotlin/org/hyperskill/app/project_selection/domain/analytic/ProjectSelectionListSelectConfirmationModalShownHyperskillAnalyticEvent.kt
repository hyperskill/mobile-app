package org.hyperskill.app.project_selection.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a shown analytic event for the project selection confirmation modal.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/tracks/1/projects"
 *     "action": "shown",
 *     "part": "modal",
 *     "target": "project_selection_modal"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class ProjectSelectionListSelectConfirmationModalShownHyperskillAnalyticEvent(
    trackId: Long
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Tracks.Projects(trackId),
    HyperskillAnalyticAction.SHOWN,
    HyperskillAnalyticPart.MODAL,
    HyperskillAnalyticTarget.PROJECT_SELECTION_MODAL
)