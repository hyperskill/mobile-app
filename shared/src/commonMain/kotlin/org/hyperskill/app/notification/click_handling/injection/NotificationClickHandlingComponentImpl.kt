package org.hyperskill.app.notification.click_handling.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingDispatcher
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingReducer

class NotificationClickHandlingComponentImpl(private val appGraph: AppGraph) : NotificationClickHandlingComponent {
    override val notificationClickHandlingReducer: NotificationClickHandlingReducer
        get() = NotificationClickHandlingReducer()
    override val notificationClickHandlingDispatcher: NotificationClickHandlingDispatcher
        get() = NotificationClickHandlingDispatcher(
            ActionDispatcherOptions(),
            appGraph.analyticComponent.analyticInteractor,
            appGraph.profileDataComponent.currentProfileStateRepository,
            appGraph.buildBadgesDataComponent().badgesRepository,
            appGraph.sentryComponent.sentryInteractor
        )
}