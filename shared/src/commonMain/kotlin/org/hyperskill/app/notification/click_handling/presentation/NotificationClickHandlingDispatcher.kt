package org.hyperskill.app.notification.click_handling.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature.Action
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class NotificationClickHandlingDispatcher(
    scopeConfig: ActionDispatcherOptions,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(scopeConfig.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is NotificationClickHandlingFeature.InternalAction.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.event)
            else -> {
                // no op
            }
        }
    }
}