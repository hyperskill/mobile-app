package org.hyperskill.app.notification.remote.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.notification.remote.domain.model.PushNotificationData

/**
 * Represents a shown analytic event of the push notification.
 *
 * When the user sees the push notification in the notification center.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "None",
 *     "action": "shown",
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
class PushNotificationShownHyperskillAnalyticEvent(
    private val pushNotificationData: PushNotificationData
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.None,
    action = HyperskillAnalyticAction.SHOWN,
    part = HyperskillAnalyticPart.NOTIFICATION,
    context = PushNotificationDataAnalyticContextMapper.map(pushNotificationData)
)