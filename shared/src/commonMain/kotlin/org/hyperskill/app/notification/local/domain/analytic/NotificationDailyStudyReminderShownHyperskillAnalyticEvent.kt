package org.hyperskill.app.notification.local.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import ru.nobird.app.core.model.mapOfNotNull

/**
 * Represents show of the local daily study notification analytic event.
 * When the local notifications scheduled.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "None",
 *     "action": "shown",
 *     "part": "notification",
 *     "target": "daily_notification",
 *     "context":
 *     {
 *         "key": 10,
 *         "planned_at": "2022-12-09T09:00:00Z"
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class NotificationDailyStudyReminderShownHyperskillAnalyticEvent(
    private val notificationId: Int,
    private val plannedAtISO8601: String?
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.None,
    action = HyperskillAnalyticAction.SHOWN,
    part = HyperskillAnalyticPart.NOTIFICATION,
    target = HyperskillAnalyticTarget.DAILY_NOTIFICATION,
    context = mapOfNotNull(
        PARAM_KEY to notificationId,
        PLANNED_AT to plannedAtISO8601
    )
) {
    companion object {
        private const val PARAM_KEY = "key"
        private const val PLANNED_AT = "planned_at"
    }
}