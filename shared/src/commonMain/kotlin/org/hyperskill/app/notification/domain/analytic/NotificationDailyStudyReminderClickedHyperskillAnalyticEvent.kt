package org.hyperskill.app.notification.domain.analytic

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
 *     "route": "/home",
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
    private val notificationId: Int
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Home(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.NOTIFICATION,
    HyperskillAnalyticTarget.DAILY_NOTIFICATION
) {
    companion object {
        private const val PARAM_KEY = "key"
    }

    override val params: Map<String, Any>
        get() = super.params + mapOf(
            PARAM_CONTEXT to mapOf(
                PARAM_KEY to notificationId
            )
        )
}