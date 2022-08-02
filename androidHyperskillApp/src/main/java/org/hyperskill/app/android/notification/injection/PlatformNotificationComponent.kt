package org.hyperskill.app.android.notification.injection

import org.hyperskill.app.android.notification.HyperskillNotificationManager
import org.hyperskill.app.android.notification.MockNotificationDelegate
import org.hyperskill.app.android.notification.NotificationDelegate
import org.hyperskill.app.android.notification.NotificationPublisher

interface PlatformNotificationComponent {
    val notificationManager: HyperskillNotificationManager
    val notificationPublisher: NotificationPublisher
    // TODO remove before merging to develop
    val mockNotificationDelegate: MockNotificationDelegate

    fun getNotificationDelegates(): Set<NotificationDelegate> =
        setOf(mockNotificationDelegate)
}