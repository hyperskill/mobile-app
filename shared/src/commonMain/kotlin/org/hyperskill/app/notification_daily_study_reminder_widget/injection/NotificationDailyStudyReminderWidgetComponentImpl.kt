package org.hyperskill.app.notification_daily_study_reminder_widget.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetActionDispatcher
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetReducer

internal class NotificationDailyStudyReminderWidgetComponentImpl(
    private val appGraph: AppGraph
) : NotificationDailyStudyReminderWidgetComponent {
    override val notificationDailyStudyReminderWidgetReducer: NotificationDailyStudyReminderWidgetReducer
        get() = NotificationDailyStudyReminderWidgetReducer()

    /* ktlint-disable */
    override val notificationDailyStudyReminderWidgetActionDispatcher: NotificationDailyStudyReminderWidgetActionDispatcher
        get() = NotificationDailyStudyReminderWidgetActionDispatcher(
            config = ActionDispatcherOptions(),
            analyticInteractor = appGraph.analyticComponent.analyticInteractor
        )
}