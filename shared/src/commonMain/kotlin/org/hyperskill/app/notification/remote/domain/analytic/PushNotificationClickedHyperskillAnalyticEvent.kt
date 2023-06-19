package org.hyperskill.app.notification.remote.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the remote push notification analytic event.
 *
 * When the user interacts with remote notifications through the notification center or pop-up.
 *
 * @constructor Creates an analytic event with the given [target].
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/home",
 *     "action": "click",
 *     "part": "notification",
 *     "target": "STREAK_THREE" || "SELECT_FIRST_TRACK" || ...,
 *     "context":
 *     {
 *         "group": "Routine learning" || "Re-engagement" || ...
 *     }
 * }
 * ```
 *
 * @param target The target of the analytic event.
 * @param group The group of the analytic event.
 */
class PushNotificationClickedHyperskillAnalyticEvent(
    target: HyperskillAnalyticTarget,
    private val group: String
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Home(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.NOTIFICATION,
    target
) {
    companion object {
        private const val PARAM_GROUP = "group"
    }

    override val params: Map<String, Any>
        get() = super.params + mapOf(
            PARAM_CONTEXT to mapOf(
                PARAM_GROUP to group
            )
        )
}