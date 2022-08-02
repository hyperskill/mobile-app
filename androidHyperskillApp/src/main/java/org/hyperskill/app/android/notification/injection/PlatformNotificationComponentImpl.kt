package org.hyperskill.app.android.notification.injection

import android.content.Context
import com.russhwolf.settings.Settings
import org.hyperskill.app.android.notification.HyperskillNotificationManager
import org.hyperskill.app.android.notification.HyperskillNotificationManagerImpl
import org.hyperskill.app.android.notification.NotificationPublisher
import org.hyperskill.app.android.notification.NotificationPublisherImpl
import org.hyperskill.app.android.notification.MockNotificationDelegate

class PlatformNotificationComponentImpl(
    private val context: Context,
    private val settings: Settings
) : PlatformNotificationComponent {
    override val notificationManager: HyperskillNotificationManager
        get() = HyperskillNotificationManagerImpl(context, settings)

    override val notificationPublisher: NotificationPublisher
        get() = NotificationPublisherImpl()

    override val mockNotificationDelegate: MockNotificationDelegate
        get() = MockNotificationDelegate(context, notificationManager)
}