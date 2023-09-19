package org.hyperskill.app.notification_onboarding.domain.analytics

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

/**
 * Represents a view analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/onboarding/notifications",
 *     "action": "view"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
object NotificationsOnboardingViewedHyperskillAnalyticsEvent :
    HyperskillAnalyticEvent(HyperskillAnalyticRoute.Onboarding.Notifications, HyperskillAnalyticAction.VIEW)