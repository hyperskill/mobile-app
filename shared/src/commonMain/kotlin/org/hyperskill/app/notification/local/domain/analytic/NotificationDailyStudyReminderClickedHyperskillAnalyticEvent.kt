package org.hyperskill.app.notification.local.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the local daily study notification analytic event.
 *
 * When the user interacts with local notifications through the notification center or pop-up.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "None",
 *     "action": "click",
 *     "part": "notification",
 *     "target": "daily_notification",
 *     "context":
 *     {
 *         "key": 10
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class NotificationDailyStudyReminderClickedHyperskillAnalyticEvent(
    notificationId: Int
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.None,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.NOTIFICATION,
    target = HyperskillAnalyticTarget.DAILY_NOTIFICATION,
    context = mapOf(PARAM_KEY to notificationId)
) {
    companion object {
        private const val PARAM_KEY = "key"
    }
}