package org.hyperskill.app.android.notification.injection

import org.hyperskill.app.android.notification.HyperskillNotificationManager
import org.hyperskill.app.android.notification.NotificationDelegate
import org.hyperskill.app.android.notification.NotificationPublisher
import org.hyperskill.app.android.notification.DailyStudyReminderNotificationDelegate
import org.hyperskill.app.notification.domain.interactor.NotificationInteractor

interface PlatformNotificationComponent {
    val notificationInteractor: NotificationInteractor
    val notificationManager: HyperskillNotificationManager
    val notificationPublisher: NotificationPublisher
    val dailyStudyReminderNotificationDelegate: DailyStudyReminderNotificationDelegate

    fun getNotificationDelegates(): Set<NotificationDelegate> =
        setOf(dailyStudyReminderNotificationDelegate)
}