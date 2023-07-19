package org.hyperskill.app.profile.domain.analytic.badges

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.badges.domain.model.BadgeKind

/**
 * Represents click on the daily study reminds formatted time button analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/profile",
 *     "action": "click",
 *     "part": "main",
 *     "target": "badge_card",
 *     "context": {
 *         "badge_kind": "Project Mastery",
 *         "is_locked": true
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class ProfileClickedBadgeCardHyperskillAnalyticsEvent(
    private val badgeKind: BadgeKind,
    private val isLocked: Boolean
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Profile(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.BADGE_CARD
) {
    companion object {
        private const val PARAM_BADGE_KIND = "badge_kind"
        private const val PARAM_LOCKED = "is_locked"
    }

    override val params: Map<String, Any>
        get() = super.params + mapOf(
            PARAM_CONTEXT to mapOf(
                PARAM_BADGE_KIND to badgeKind.name,
                PARAM_LOCKED to isLocked
            )
        )
}