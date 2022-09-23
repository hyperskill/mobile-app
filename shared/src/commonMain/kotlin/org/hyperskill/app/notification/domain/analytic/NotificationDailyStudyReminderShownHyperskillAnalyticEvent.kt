package org.hyperskill.app.notification.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import ru.nobird.app.core.model.mapOfNotNull

class NotificationDailyStudyReminderShownHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute,
    private val notificationId: Int,
    private val plannedAtISO8601: String?
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.SHOWN,
    HyperskillAnalyticPart.NOTIFICATION,
    HyperskillAnalyticTarget.DAILY_NOTIFICATION
) {
    companion object {
        private const val PARAM_KEY = "key"
        private const val PLANNED_AT = "planned_at"
    }

    override val params: Map<String, Any>
        get() = super.params + mapOf(
            PARAM_CONTEXT to mapOfNotNull(
                PARAM_KEY to notificationId,
                PLANNED_AT to plannedAtISO8601
            )
        )
}