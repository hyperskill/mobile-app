package org.hyperskill.app.streak_recovery.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the "No, thanks" button analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/home",
 *     "action": "click",
 *     "part": "streak_recovery_modal",
 *     "target": "no_thanks"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class StreakRecoveryModalClickedNoThanksHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Home(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.STREAK_RECOVERY_MODAL,
    HyperskillAnalyticTarget.NO_THANKS
)