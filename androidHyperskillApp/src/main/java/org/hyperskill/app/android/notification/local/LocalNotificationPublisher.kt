package org.hyperskill.app.android.notification.local

interface LocalNotificationPublisher {
    fun onNeedShowNotificationWithId(id: String)
}