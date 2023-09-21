package org.hyperskill.app.notification.remote.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.notification.remote.domain.model.PushNotificationData

/**
 * Represents click on the remote push notification analytic event.
 *
 * When the user interacts with remote notifications through the notification center or pop-up.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/home",
 *     "action": "click",
 *     "part": "notification",
 *     "context":
 *     {
 *         "type": "STREAK_THREE" || "SELECT_FIRST_TRACK" || ...,
 *         "category": "Routine learning" || "Re-engagement" || ...,
 *         "image": "https://hyperskill.org/image.png",
 *         "notification_id": "12345"
 *     }
 * }
 * ```
 *
 * @property pushNotificationData The data of the push notification.
 */
class PushNotificationClickedHyperskillAnalyticEvent(
    private val pushNotificationData: PushNotificationData
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Home(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.NOTIFICATION
) {
    override val params: Map<String, Any>
        get() = super.params + mapOf(
            PARAM_CONTEXT to PushNotificationDataAnalyticContextMapper.map(pushNotificationData)
        )
}