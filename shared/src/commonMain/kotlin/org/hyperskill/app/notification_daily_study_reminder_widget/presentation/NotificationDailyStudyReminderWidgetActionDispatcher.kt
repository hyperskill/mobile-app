package org.hyperskill.app.notification_daily_study_reminder_widget.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.presentation.SingleAnalyticEventActionDispatcher
import org.hyperskill.app.core.presentation.CompositeActionDispatcher
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetFeature.Action
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetFeature.InternalAction
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetFeature.Message

class NotificationDailyStudyReminderWidgetActionDispatcher internal constructor(
    mainNotificationDailyStudyReminderWidgetActionDispatcher: MainNotificationDailyStudyReminderWidgetActionDispatcher,
    analyticInteractor: AnalyticInteractor
) : CompositeActionDispatcher<Action, Message>(
    listOf(
        mainNotificationDailyStudyReminderWidgetActionDispatcher,
        SingleAnalyticEventActionDispatcher(analyticInteractor) {
            (it as? InternalAction.LogAnalyticEvent)?.event
        }
    )
)