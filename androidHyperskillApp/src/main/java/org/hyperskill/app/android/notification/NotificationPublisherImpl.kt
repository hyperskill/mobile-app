package org.hyperskill.app.android.notification

class NotificationPublisherImpl(
    private val notificationDelegates: Set<@JvmSuppressWildcards NotificationDelegate>
) : NotificationPublisher {

    override fun onNeedShowNotificationWithId(id: String) {
        notificationDelegates
            .find { it.id == id }
            ?.onNeedShowNotification()
    }
}