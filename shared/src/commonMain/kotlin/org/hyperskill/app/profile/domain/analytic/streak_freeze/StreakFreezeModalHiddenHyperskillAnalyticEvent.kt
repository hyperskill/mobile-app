package org.hyperskill.app.profile.domain.analytic.streak_freeze

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents hidden analytic event of the streak freeze modal.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/profile",
 *     "action": "hidden",
 *     "part": "streak_freeze_modal",
 *     "target": "cancel"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class StreakFreezeModalHiddenHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Profile(),
    HyperskillAnalyticAction.HIDDEN,
    HyperskillAnalyticPart.STREAK_FREEZE_MODAL,
    HyperskillAnalyticTarget.CANCEL
)