package org.hyperskill.app.notification.click_handling.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingActionDispatcher
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingReducer

internal class NotificationClickHandlingComponentImpl(
    private val appGraph: AppGraph
) : NotificationClickHandlingComponent {
    override val notificationClickHandlingReducer: NotificationClickHandlingReducer
        get() = NotificationClickHandlingReducer()

    override val notificationClickHandlingActionDispatcher: NotificationClickHandlingActionDispatcher
        get() = NotificationClickHandlingActionDispatcher(
            ActionDispatcherOptions(),
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            currentProfileStateRepository = appGraph.profileDataComponent.currentProfileStateRepository,
            nextLearningActivityStateRepository = appGraph.stateRepositoriesComponent
                .nextLearningActivityStateRepository,
            badgesRepository = appGraph.buildBadgesDataComponent().badgesRepository,
            logger = appGraph.loggerComponent.logger
        )
}