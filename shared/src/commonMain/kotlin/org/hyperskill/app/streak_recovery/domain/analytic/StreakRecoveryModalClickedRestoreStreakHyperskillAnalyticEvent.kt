package org.hyperskill.app.streak_recovery.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the "Restore streak" button analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "None",
 *     "action": "click",
 *     "part": "streak_recovery_modal",
 *     "target": "restore_streak"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class StreakRecoveryModalClickedRestoreStreakHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.None,
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.STREAK_RECOVERY_MODAL,
    HyperskillAnalyticTarget.RESTORE_STREAK
)