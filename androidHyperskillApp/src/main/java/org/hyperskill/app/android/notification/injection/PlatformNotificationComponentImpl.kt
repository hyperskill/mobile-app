package org.hyperskill.app.android.notification.injection

import android.content.Context
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.notification.*
import org.hyperskill.app.android.notification.DailyStudyReminderNotificationDelegate
import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.notification.domain.NotificationInteractor

class PlatformNotificationComponentImpl(
    private val context: Context,
    private val appGraph: AppGraph
) : PlatformNotificationComponent {
    override val notificationInteractor: NotificationInteractor
        get() = appGraph.buildNotificationComponent().notificationInteractor

    override val notificationManager: HyperskillNotificationManager
        get() = HyperskillNotificationManagerImpl(context, notificationInteractor)

    override val notificationPublisher: NotificationPublisher
        get() = NotificationPublisherImpl(HyperskillApp.graph().platformNotificationComponent.getNotificationDelegates())

    override val dailyStudyReminderNotificationDelegate: DailyStudyReminderNotificationDelegate
        get() = DailyStudyReminderNotificationDelegate(context, notificationManager, notificationInteractor)
}