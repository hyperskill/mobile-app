package org.hyperskill.app.notification.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

class NotificationSystemNoticeHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute,
    isAllowed: Boolean
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.HIDDEN,
    HyperskillAnalyticPart.NOTIFICATIONS_SYSTEM_NOTICE,
    if (isAllowed) HyperskillAnalyticTarget.ALLOW else HyperskillAnalyticTarget.DENY
)