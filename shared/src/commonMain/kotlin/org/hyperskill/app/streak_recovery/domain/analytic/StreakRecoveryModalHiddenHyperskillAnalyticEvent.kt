package org.hyperskill.app.streak_recovery.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a hidden analytic event of the streak recovery modal.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/home",
 *     "action": "hidden",
 *     "part": "streak_recovery_modal",
 *     "target": "close"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class StreakRecoveryModalHiddenHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Home(),
    HyperskillAnalyticAction.HIDDEN,
    HyperskillAnalyticPart.STREAK_RECOVERY_MODAL,
    HyperskillAnalyticTarget.CLOSE
)