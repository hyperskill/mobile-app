package org.hyperskill.app.profile.domain.analytic.streak_freeze

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents shown analytic event of the streak freeze modal.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/profile",
 *     "action": "shown",
 *     "part": "modal",
 *     "target": "streak_freeze_modal"
 *     "context":
 *     {
 *         "state": "can_buy / already_have / not_enough_gems"
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class StreakFreezeModalShownHyperskillAnalyticEvent(
    private val streakFreezeState: StreakFreezeAnalyticState
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Profile(),
    HyperskillAnalyticAction.SHOWN,
    HyperskillAnalyticPart.MODAL,
    HyperskillAnalyticTarget.STREAK_FREEZE_MODAL
) {
    companion object {
        private const val PARAM_STATE = "state"
    }

    override val params: Map<String, Any>
        get() = super.params + mapOf(PARAM_CONTEXT to mapOf(PARAM_STATE to streakFreezeState.stringValue))
}