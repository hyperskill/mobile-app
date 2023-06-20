package org.hyperskill.app.android.notification.local

import android.app.Notification

interface HyperskillNotificationManager {
    fun scheduleNotification(id: String, millis: Long)

    /**
     * Reschedule notification using its original schedule time, used in [scheduleNotification] call
     * If original schedule time is expired, then [nextMillis] will be used.
     * */
    fun rescheduleActiveNotification(id: String, nextMillis: Long?)
    fun showNotification(id: Long, notification: Notification)
}