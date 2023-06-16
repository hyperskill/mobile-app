package org.hyperskill.app.android.notification.injection

import android.content.Context
import org.hyperskill.app.android.notification.DailyStudyReminderNotificationDelegate
import org.hyperskill.app.android.notification.HyperskillNotificationManager
import org.hyperskill.app.android.notification.HyperskillNotificationManagerImpl
import org.hyperskill.app.android.notification.NotificationPublisher
import org.hyperskill.app.android.notification.NotificationPublisherImpl
import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.notification.local.domain.interactor.NotificationInteractor

class PlatformNotificationComponentImpl(
    private val context: Context,
    private val appGraph: AppGraph
) : PlatformNotificationComponent {
    override val notificationInteractor: NotificationInteractor
        get() = appGraph.buildNotificationComponent().notificationInteractor

    override val notificationManager: HyperskillNotificationManager
        get() = HyperskillNotificationManagerImpl(context, notificationInteractor)

    override val notificationPublisher: NotificationPublisher
        get() = NotificationPublisherImpl(getNotificationDelegates())

    override val dailyStudyReminderNotificationDelegate: DailyStudyReminderNotificationDelegate
        get() = DailyStudyReminderNotificationDelegate(
            hyperskillNotificationManager = notificationManager,
            context = context,
            notificationInteractor = notificationInteractor,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor
        )
}