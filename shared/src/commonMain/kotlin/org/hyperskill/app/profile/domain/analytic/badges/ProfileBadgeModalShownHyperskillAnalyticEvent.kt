package org.hyperskill.app.profile.domain.analytic.badges

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.badges.domain.model.BadgeKind

/**
 * Represents click on the badge in profile analytics event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/profile",
 *     "action": "shown",
 *     "part": "modal",
 *     "target": "badge_modal",
 *     "context": {
 *         "badge_kind": "Project Mastery"
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class ProfileBadgeModalShownHyperskillAnalyticEvent(
    private val badgeKind: BadgeKind
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Profile(),
    HyperskillAnalyticAction.SHOWN,
    HyperskillAnalyticPart.MODAL,
    HyperskillAnalyticTarget.BADGE_MODAL
) {
    override val params: Map<String, Any>
        get() = super.params + mapOf(
            BadgesAnalyticKeys.PARAM_BADGE_KIND to badgeKind
        )
}