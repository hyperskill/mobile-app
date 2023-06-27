package org.hyperskill.app.notification.local.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a shown analytic event of the system prompt to authorize the requested interactions with the local
 * and remote notifications for the app.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/home",
 *     "action": "shown",
 *     "part": "notice",
 *     "target": "notifications_system_notice"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class NotificationSystemNoticeShownHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.SHOWN,
    HyperskillAnalyticPart.NOTICE,
    HyperskillAnalyticTarget.NOTIFICATIONS_SYSTEM_NOTICE
)