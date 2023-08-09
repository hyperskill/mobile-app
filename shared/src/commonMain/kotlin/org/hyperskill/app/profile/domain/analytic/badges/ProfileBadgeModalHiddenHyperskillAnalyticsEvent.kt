package org.hyperskill.app.profile.domain.analytic.badges

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.badges.domain.model.BadgeKind

/**
 * Represents a hidden analytic event of the badge detailed modal in profile analytics event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/profile",
 *     "action": "hidden",
 *     "part": "modal",
 *     "target": "badge_modal",
 *     "context": {
 *         "badge_kind": "Project Mastery"
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class ProfileBadgeModalHiddenHyperskillAnalyticsEvent(
    private val badgeKind: BadgeKind
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Profile(),
    HyperskillAnalyticAction.HIDDEN,
    HyperskillAnalyticPart.MODAL,
    HyperskillAnalyticTarget.BADGE_MODAL
) {
    override val params: Map<String, Any>
        get() = super.params + mapOf(
            BadgesAnalyticKeys.PARAM_BADGE_KIND to badgeKind.name
        )
}