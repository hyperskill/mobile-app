package org.hyperskill.app.track.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the "Continue learning on the web" button analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/track",
 *     "action": "click",
 *     "part": "description",
 *     "target": "continue_to_hyperskill"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class TrackClickedContinueInWebHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Track(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.DESCRIPTION,
    HyperskillAnalyticTarget.CONTINUE_TO_HYPERSKILL
)