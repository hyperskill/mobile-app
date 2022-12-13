package org.hyperskill.app.placeholder_new_user.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a shown analytic event of the new user placeholder track modal.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/register",
 *     "action": "shown",
 *     "part": "modal",
 *     "target": "track_modal",
 *     "context":
 *     {
 *         "track_id": 1234
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class PlaceholderNewUserTrackModalShownHyperskillAnalyticEvent(val trackId: Long) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Register(),
    HyperskillAnalyticAction.SHOWN,
    HyperskillAnalyticPart.MODAL,
    HyperskillAnalyticTarget.TRACK_MODAL
) {
    companion object {
        private const val PARAM_TRACK_ID = "track_id"
    }

    override val params: Map<String, Any>
        get() = super.params +
            mapOf(PARAM_CONTEXT to mapOf(PARAM_TRACK_ID to trackId))
}