package org.hyperskill.app.progress_screen.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a click on "Change track" button analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/progress",
 *     "action": "click",
 *     "part": "main",
 *     "target": "change_track"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class ProgressScreenClickedChangeTrackHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Progress(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.CHANGE_TRACK
)