package org.hyperskill.app.notification.local.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a hidden analytic event of the system prompt to authorize the requested interactions with the local
 * and remote notifications for the app.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/home",
 *     "action": "hidden",
 *     "part": "notifications_system_notice",
 *     "target": "allow / deny"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class NotificationSystemNoticeHiddenHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute,
    isAllowed: Boolean
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.HIDDEN,
    HyperskillAnalyticPart.NOTIFICATIONS_SYSTEM_NOTICE,
    target = if (isAllowed) HyperskillAnalyticTarget.ALLOW else HyperskillAnalyticTarget.DENY
)