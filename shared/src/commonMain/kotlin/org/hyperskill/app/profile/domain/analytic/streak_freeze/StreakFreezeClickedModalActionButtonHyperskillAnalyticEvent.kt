package org.hyperskill.app.profile.domain.analytic.streak_freeze

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

/**
 * Represents click on the action button in the streak freeze modal analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/profile",
 *     "action": "click",
 *     "part": "streak_freeze_modal",
 *     "target": "get_it / continue_learning"
 *     "context":
 *     {
 *         "state": "can_buy / already_have / not_enough_games"
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class StreakFreezeClickedModalActionButtonHyperskillAnalyticEvent(
    modalAction: StreakFreezeModalAnalyticAction,
    private val streakFreezeState: StreakFreezeAnalyticState
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Profile(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.STREAK_FREEZE_MODAL,
    modalAction.analyticTarget
) {
    companion object {
        private const val PARAM_STATE = "state"
    }

    override val params: Map<String, Any>
        get() = super.params + mapOf(PARAM_CONTEXT to mapOf(PARAM_STATE to streakFreezeState.stringValue))
}