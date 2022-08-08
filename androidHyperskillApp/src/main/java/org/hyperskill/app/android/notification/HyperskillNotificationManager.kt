package org.hyperskill.app.android.notification

import android.app.Notification

interface HyperskillNotificationManager {
    fun scheduleNotification(id: String, millis: Long)
    fun rescheduleActiveNotification(id: String)
    fun showNotification(id: Long, notification: Notification)
}