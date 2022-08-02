package org.hyperskill.app.android.notification

import org.hyperskill.app.android.HyperskillApp

class NotificationPublisherImpl : NotificationPublisher {
    private var notificationDelegates: Set<@JvmSuppressWildcards NotificationDelegate> =
        HyperskillApp.graph().platformNotificationComponent.getNotificationDelegates()

    override fun onNeedShowNotificationWithId(id: String) {
        notificationDelegates
            .find { it.id == id }
            ?.onNeedShowNotification()
    }
}