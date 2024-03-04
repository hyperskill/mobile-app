package org.hyperskill.app.challenges.widget.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import ru.nobird.app.core.model.mapOfNotNull

/**
 * Represents a click analytic event on the "Collect Reward" button.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/home",
 *     "action": "click",
 *     "part": "challenge_card",
 *     "target": "collect_reward",
 *     "context":
 *     {
 *         "challenge_id": 1
 *     }
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class ChallengeWidgetClickedCollectRewardHyperskillAnalyticEvent(
    val challengeId: Long?
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Home(),
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.CHALLENGE_CARD,
    target = HyperskillAnalyticTarget.COLLECT_REWARD,
    context = mapOfNotNull(ChallengeWidgetAnalyticParams.PARAM_CHALLENGE_ID to challengeId)
)