package org.hyperskill.app.challenges.widget.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a click analytic event of the error state placeholder retry button.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/home",
 *     "action": "click",
 *     "part": "challenge_card",
 *     "target": "retry"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
object ChallengeWidgetClickedRetryContentLoadingHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Home(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.CHALLENGE_CARD,
    HyperskillAnalyticTarget.RETRY
)