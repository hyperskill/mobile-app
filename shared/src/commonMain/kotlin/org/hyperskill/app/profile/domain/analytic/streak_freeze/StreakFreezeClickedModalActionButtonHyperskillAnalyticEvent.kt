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
 *         "state": "can_buy / already_have / not_enough_gems"
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class StreakFreezeClickedModalActionButtonHyperskillAnalyticEvent(
    modalAction: StreakFreezeModalAnalyticAction,
    streakFreezeState: StreakFreezeAnalyticState
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Profile(),
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.STREAK_FREEZE_MODAL,
    target = modalAction.analyticTarget,
    context = mapOf(PARAM_STATE to streakFreezeState.stringValue)
) {
    companion object {
        private const val PARAM_STATE = "state"
    }
}