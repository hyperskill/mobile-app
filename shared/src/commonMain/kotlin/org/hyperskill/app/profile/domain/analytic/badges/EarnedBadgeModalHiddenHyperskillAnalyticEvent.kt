package org.hyperskill.app.profile.domain.analytic.badges

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.badges.domain.model.BadgeKind

/**
 * Represents a hidden analytic event of the earned badge modal analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/profile",
 *     "action": "hidden",
 *     "part": "modal",
 *     "target": "earned_badge_modal",
 *     "context": {
 *         "badge_kind": "Project Mastery"
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class EarnedBadgeModalHiddenHyperskillAnalyticEvent(
    badgeKind: BadgeKind
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Profile(),
    action = HyperskillAnalyticAction.HIDDEN,
    part = HyperskillAnalyticPart.MODAL,
    target = HyperskillAnalyticTarget.EARNED_BADGE_MODAL,
    context = mapOf(BadgesAnalyticKeys.PARAM_BADGE_KIND to badgeKind.name)
)