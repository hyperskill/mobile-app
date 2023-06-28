package org.hyperskill.app.android.notification.local

class LocalNotificationPublisherImpl(
    private val localNotificationDelegates: Set<@JvmSuppressWildcards LocalNotificationDelegate>
) : LocalNotificationPublisher {

    override fun onNeedShowNotificationWithId(id: String) {
        localNotificationDelegates
            .find { it.id == id }
            ?.onNeedShowNotification()
    }
}