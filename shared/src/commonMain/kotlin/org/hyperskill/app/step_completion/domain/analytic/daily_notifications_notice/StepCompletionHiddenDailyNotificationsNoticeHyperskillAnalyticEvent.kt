package org.hyperskill.app.step_completion.domain.analytic.daily_notifications_notice

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a hidden analytic event of the prompt to receive daily study reminders.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "hidden",
 *     "part": "daily_notifications_notice",
 *     "target": "ok / later"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class StepCompletionHiddenDailyNotificationsNoticeHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute,
    isAgreed: Boolean
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.HIDDEN,
    HyperskillAnalyticPart.DAILY_NOTIFICATIONS_NOTICE,
    target = if (isAgreed) HyperskillAnalyticTarget.OK else HyperskillAnalyticTarget.LATER
)