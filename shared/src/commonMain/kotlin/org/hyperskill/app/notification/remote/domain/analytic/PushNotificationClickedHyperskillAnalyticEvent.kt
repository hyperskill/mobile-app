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
 *         "group": "Routine learning" || "Re-engagement" || ...
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
            PARAM_CONTEXT to mapOf(
                PushNotificationHyperskillAnalyticParams.PARAM_TYPE to pushNotificationData.typeString,
                PushNotificationHyperskillAnalyticParams.PARAM_CATEGORY to pushNotificationData.categoryString,
                PushNotificationHyperskillAnalyticParams.PARAM_IMAGE to pushNotificationData.image
            )
        )
}