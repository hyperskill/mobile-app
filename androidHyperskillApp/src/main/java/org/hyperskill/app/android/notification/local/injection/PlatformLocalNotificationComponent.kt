package org.hyperskill.app.android.notification.local.injection

import org.hyperskill.app.android.notification.local.DailyStudyReminderLocalNotificationDelegate
import org.hyperskill.app.android.notification.local.HyperskillNotificationManager
import org.hyperskill.app.android.notification.local.LocalNotificationDelegate
import org.hyperskill.app.android.notification.local.LocalNotificationPublisher
import org.hyperskill.app.notification.local.domain.interactor.NotificationInteractor

interface PlatformLocalNotificationComponent {
    val notificationInteractor: NotificationInteractor
    val notificationManager: HyperskillNotificationManager
    val localNotificationPublisher: LocalNotificationPublisher
    val dailyStudyReminderNotificationDelegate: DailyStudyReminderLocalNotificationDelegate

    fun getNotificationDelegates(): Set<LocalNotificationDelegate> =
        setOf(dailyStudyReminderNotificationDelegate)
}