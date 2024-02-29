package org.hyperskill.app.profile.domain.analytic.badges

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.badges.domain.model.BadgeKind

/**
 * Represents click on the badge in profile analytic event.
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
 *
 * @see HyperskillAnalyticEvent
 */
class ProfileClickedBadgeCardHyperskillAnalyticEvent(
    badgeKind: BadgeKind,
    isLocked: Boolean
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Profile(),
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.MAIN,
    target = HyperskillAnalyticTarget.BADGE_CARD,
    context = mapOf(
        BadgesAnalyticKeys.PARAM_BADGE_KIND to badgeKind.name,
        BadgesAnalyticKeys.PARAM_LOCKED to isLocked
    )
)