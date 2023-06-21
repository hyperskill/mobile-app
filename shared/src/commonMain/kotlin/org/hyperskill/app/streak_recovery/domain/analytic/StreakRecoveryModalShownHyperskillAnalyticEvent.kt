package org.hyperskill.app.streak_recovery.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a shown analytic event of the streak recovery modal.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/home",
 *     "action": "shown",
 *     "part": "modal",
 *     "target": "streak_recovery_modal"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class StreakRecoveryModalShownHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Home(),
    HyperskillAnalyticAction.SHOWN,
    HyperskillAnalyticPart.MODAL,
    HyperskillAnalyticTarget.STREAK_RECOVERY_MODAL
)