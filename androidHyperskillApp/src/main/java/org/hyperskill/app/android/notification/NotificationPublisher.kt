package org.hyperskill.app.android.notification

interface NotificationPublisher {
    fun onNeedShowNotificationWithId(id: String)
}