package org.hyperskill.app.notifications_onboarding.domain.analytics

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the "Remind me later" button analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/onboarding/notifications",
 *     "action": "click",
 *     "part": "main",
 *     "target": "remind_me_later"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
object NotificationsOnboardingClickedRemindMeLaterHyperskillAnalyticsEvent : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Onboarding.Notifications,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.MAIN,
    target = HyperskillAnalyticTarget.REMIND_ME_LATER
)