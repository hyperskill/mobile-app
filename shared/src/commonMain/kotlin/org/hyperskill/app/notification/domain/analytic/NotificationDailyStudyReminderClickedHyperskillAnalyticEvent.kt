package org.hyperskill.app.notification.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

class NotificationDailyStudyReminderClickedHyperskillAnalyticEvent(
    private val isNotificationPermissionGranted: Boolean,
    private val notificationId: Int
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Home(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.NOTIFICATION,
    HyperskillAnalyticTarget.DAILY_NOTIFICATION
) {
    companion object {
        private const val PARAM_IS_NOTIFICATIONS_ALLOW = "is_notifications_allow"
        private const val PARAM_KEY = "key"
    }

    override val params: Map<String, Any>
        get() = super.params + mapOf(
            PARAM_CONTEXT to mapOf(
                PARAM_IS_NOTIFICATIONS_ALLOW to isNotificationPermissionGranted,
                PARAM_KEY to notificationId
            )
        )
}