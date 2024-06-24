package org.hyperskill.app.notification.click_handling.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.presentation.SingleAnalyticEventActionDispatcher
import org.hyperskill.app.core.presentation.CompositeActionDispatcher
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature.Action
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature.InternalAction
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature.Message

class NotificationClickHandlingActionDispatcher internal constructor(
    mainNotificationClickHandlingActionDispatcher: MainNotificationClickHandlingActionDispatcher,
    analyticInteractor: AnalyticInteractor
) : CompositeActionDispatcher<Action, Message>(
    listOf(
        mainNotificationClickHandlingActionDispatcher,
        SingleAnalyticEventActionDispatcher(analyticInteractor) {
            (it as? InternalAction.LogAnalyticEvent)?.analyticEvent
        }
    )
)