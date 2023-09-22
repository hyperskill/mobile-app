package org.hyperskill.app.notifications_onboarding.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.notification.local.domain.interactor.NotificationInteractor
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.Action
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.InternalAction
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class NotificationsOnboardingActionDispatcher(
    config: ActionDispatcherOptions,
    private val notificationInteractor: NotificationInteractor,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            InternalAction.UpdateLastNotificationPermissionRequestTime ->
                notificationInteractor.updateLastTimeUserAskedToEnableDailyReminders()
            is InternalAction.LogAnalyticsEvent ->
                analyticInteractor.logEvent(action.event)
            else -> {
                // no op
            }
        }
    }
}