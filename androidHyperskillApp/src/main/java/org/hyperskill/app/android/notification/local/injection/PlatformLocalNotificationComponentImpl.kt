package org.hyperskill.app.android.notification.local.injection

import android.content.Context
import org.hyperskill.app.android.notification.local.DailyStudyReminderLocalNotificationDelegate
import org.hyperskill.app.android.notification.local.HyperskillNotificationManager
import org.hyperskill.app.android.notification.local.HyperskillNotificationManagerImpl
import org.hyperskill.app.android.notification.local.LocalNotificationPublisher
import org.hyperskill.app.android.notification.local.LocalNotificationPublisherImpl
import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.notification.local.domain.interactor.NotificationInteractor

class PlatformLocalNotificationComponentImpl(
    private val context: Context,
    private val appGraph: AppGraph
) : PlatformLocalNotificationComponent {
    override val notificationInteractor: NotificationInteractor
        get() = appGraph.buildNotificationComponent().notificationInteractor

    override val notificationManager: HyperskillNotificationManager
        get() = HyperskillNotificationManagerImpl(context, notificationInteractor)

    override val localNotificationPublisher: LocalNotificationPublisher
        get() = LocalNotificationPublisherImpl(getNotificationDelegates())

    override val dailyStudyReminderNotificationDelegate: DailyStudyReminderLocalNotificationDelegate
        get() = DailyStudyReminderLocalNotificationDelegate(
            hyperskillNotificationManager = notificationManager,
            context = context,
            notificationInteractor = notificationInteractor,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            currentProfileStateRepository = appGraph.profileDataComponent.currentProfileStateRepository
        )
}