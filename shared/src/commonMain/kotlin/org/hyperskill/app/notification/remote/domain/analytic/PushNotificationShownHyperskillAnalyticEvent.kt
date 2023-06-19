package org.hyperskill.app.notification.remote.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a shown analytic event of the push notification.
 *
 * When the user sees the push notification in the notification center.
 *
 * @property group The group of the analytic event.
 * @constructor Creates an analytic event with the given [target].
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/home",
 *     "action": "shown",
 *     "part": "notification",
 *     "target": "STREAK_THREE" || "SELECT_FIRST_TRACK" || ...,
 *     "context":
 *     {
 *         "group": "Routine learning" || "Re-engagement" || ...
 *     }
 * }
 * ```
 *
 * @param target
 */
class PushNotificationShownHyperskillAnalyticEvent(
    target: HyperskillAnalyticTarget,
    private val group: String
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Home(),
    HyperskillAnalyticAction.SHOWN,
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