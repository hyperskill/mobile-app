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
 *     "route": "/home",
 *     "action": "shown",
 *     "part": "notification",
 *     "context":
 *     {
 *         "type": "STREAK_THREE" || "SELECT_FIRST_TRACK" || ...,
 *         "group": "Routine learning" || "Re-engagement" || ...
 *     }
 * }
 * ```
 *
 * @property pushNotificationData The data of the push notification.
 */
class PushNotificationShownHyperskillAnalyticEvent(
    private val pushNotificationData: PushNotificationData
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Home(),
    HyperskillAnalyticAction.SHOWN,
    HyperskillAnalyticPart.NOTIFICATION
) {
    override val params: Map<String, Any>
        get() = super.params + mapOf(
            PARAM_CONTEXT to mapOf(
                PushNotificationHyperskillAnalyticParams.PARAM_TYPE to pushNotificationData.typeString,
                PushNotificationHyperskillAnalyticParams.PARAM_CATEGORY to pushNotificationData.categoryString
            )
        )
}