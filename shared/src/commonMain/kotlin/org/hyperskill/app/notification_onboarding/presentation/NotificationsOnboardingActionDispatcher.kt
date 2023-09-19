package org.hyperskill.app.notification_onboarding.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.notification.local.domain.interactor.NotificationInteractor
import org.hyperskill.app.notification_onboarding.presentation.NotificationsOnboardingFeature.Action
import org.hyperskill.app.notification_onboarding.presentation.NotificationsOnboardingFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class NotificationsOnboardingActionDispatcher(
    config: ActionDispatcherOptions,
    private val notificationInteractor: NotificationInteractor,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            Action.UpdateLastNotificationPermissionRequestTime ->
                notificationInteractor.updateLastTimeUserAskedToEnableDailyReminders()
            is Action.LogAnalyticsEvent ->
                analyticInteractor.logEvent(action.event)
            else -> {
                // no op
            }
        }
    }
}