package org.hyperskill.app.notification_onboarding.presentation

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.notification_onboarding.presentation.NotificationOnboardingFeature.Action
import org.hyperskill.app.notification_onboarding.presentation.NotificationOnboardingFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class NotificationOnboardingActionDispatcher(
    config: ActionDispatcherOptions
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            else -> {
                // no op
            }
        }
    }
}