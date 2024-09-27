package org.hyperskill.app.notification_daily_study_reminder_widget.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.notification_daily_study_reminder_widget.cache.NotificationDailyStudyReminderWidgetCacheDataSourceImpl
import org.hyperskill.app.notification_daily_study_reminder_widget.data.repository.NotificationDailyStudyReminderWidgetRepositoryImpl
import org.hyperskill.app.notification_daily_study_reminder_widget.data.source.NotificationDailyStudyReminderWidgetCacheDataSource
import org.hyperskill.app.notification_daily_study_reminder_widget.domain.repository.NotificationDailyStudyReminderWidgetRepository
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.MainNotificationDailyStudyReminderWidgetActionDispatcher
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetActionDispatcher
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetReducer

internal class NotificationDailyStudyReminderWidgetComponentImpl(
    private val appGraph: AppGraph
) : NotificationDailyStudyReminderWidgetComponent {
    /* ktlint-disable */
    private val notificationDailyStudyReminderWidgetCacheDataSource: NotificationDailyStudyReminderWidgetCacheDataSource =
        NotificationDailyStudyReminderWidgetCacheDataSourceImpl(appGraph.commonComponent.settings)

    private val notificationDailyStudyReminderWidgetRepository: NotificationDailyStudyReminderWidgetRepository =
        NotificationDailyStudyReminderWidgetRepositoryImpl(
            notificationDailyStudyReminderWidgetCacheDataSource
        )

    override val notificationDailyStudyReminderWidgetReducer: NotificationDailyStudyReminderWidgetReducer
        get() = NotificationDailyStudyReminderWidgetReducer()

    /* ktlint-disable */
    override val notificationDailyStudyReminderWidgetActionDispatcher: NotificationDailyStudyReminderWidgetActionDispatcher
        get() = NotificationDailyStudyReminderWidgetActionDispatcher(
            MainNotificationDailyStudyReminderWidgetActionDispatcher(
                config = ActionDispatcherOptions(),
                notificationInteractor = appGraph.buildNotificationComponent().notificationInteractor,
                notificationDailyStudyReminderWidgetRepository = notificationDailyStudyReminderWidgetRepository,
                currentProfileStateRepository = appGraph.profileDataComponent.currentProfileStateRepository
            ),
            analyticInteractor = appGraph.analyticComponent.analyticInteractor
        )
}