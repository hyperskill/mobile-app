package org.hyperskill.app.notifications_onboarding.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a shown analytic event of the daily study reminders hour interval picker modal.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/onboarding/notifications",
 *     "action": "shown",
 *     "part": "modal",
 *     "target": "daily_study_reminders_hour_interval_picker_modal"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
object NotificationsOnboardingDailyStudyRemindersIntervalPickerModalShownHyperskillAnalyticEvent :
    HyperskillAnalyticEvent(
        route = HyperskillAnalyticRoute.Onboarding.Notifications,
        action = HyperskillAnalyticAction.SHOWN,
        part = HyperskillAnalyticPart.MODAL,
        target = HyperskillAnalyticTarget.DAILY_STUDY_REMINDERS_HOUR_INTERVAL_PICKER_MODAL
    )