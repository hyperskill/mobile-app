package org.hyperskill.app.challenges.widget.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import ru.nobird.app.core.model.mapOfNotNull

/**
 * Represents a click analytic event on a link in the description block.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/home",
 *     "action": "click",
 *     "part": "challenge_card",
 *     "target": "link"
 *     "context":
 *     {
 *         "url": "https://sample.com/",
 *         "challenge_id": 1
 *     }
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class ChallengeWidgetClickedLinkInTheDescriptionHyperskillAnalyticEvent(
    val challengeId: Long?,
    val url: String
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Home(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.CHALLENGE_CARD,
    HyperskillAnalyticTarget.LINK
) {
    override val params: Map<String, Any>
        get() = super.params + mapOf(
            PARAM_CONTEXT to mapOfNotNull(
                ChallengeWidgetAnalyticParams.PARAM_CHALLENGE_ID to challengeId,
                ChallengeWidgetAnalyticParams.PARAM_URL to url
            )
        )
}