package org.hyperskill.app.android.notification.local.injection

import org.hyperskill.app.android.notification.local.HyperskillNotificationManager
import org.hyperskill.app.android.notification.local.LocalNotificationDelegate
import org.hyperskill.app.android.notification.local.LocalNotificationPublisher
import org.hyperskill.app.notification.local.domain.interactor.NotificationInteractor

interface PlatformLocalNotificationComponent {
    val notificationInteractor: NotificationInteractor
    val notificationManager: HyperskillNotificationManager
    val localNotificationPublisher: LocalNotificationPublisher

    fun getNotificationDelegates(): Set<LocalNotificationDelegate> =
        emptySet()
}