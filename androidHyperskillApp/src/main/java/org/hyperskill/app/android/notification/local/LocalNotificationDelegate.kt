package org.hyperskill.app.android.notification.local

import android.app.Notification

abstract class LocalNotificationDelegate(
    val id: String,
    private val notificationManager: HyperskillNotificationManager
) {

    abstract fun onNeedShowNotification()

    /**
     * @returns time to schedule next notification at
     * or null if it should not be scheduled
     * */
    open fun getNextScheduledAt(): Long? = null

    fun rescheduleNotification() {
        notificationManager.rescheduleActiveNotification(
            id = id,
            nextMillis = getNextScheduledAt()
        )
    }

    protected fun scheduleNotificationAt(timestamp: Long) {
        notificationManager.scheduleNotification(id, timestamp)
    }

    protected fun showNotification(id: Long, notification: Notification) {
        notificationManager.showNotification(id, notification)
    }
}