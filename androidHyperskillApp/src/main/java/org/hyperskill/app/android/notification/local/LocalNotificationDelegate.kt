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
    abstract fun getNextScheduledAt(): Long?

    abstract fun shouldRescheduleNotification(): Boolean

    open fun onRescheduleNotificationRequested() {}

    fun rescheduleNotification() {
        onRescheduleNotificationRequested()
        if (shouldRescheduleNotification()) {
            notificationManager.rescheduleActiveNotification(
                id = id,
                nextMillis = getNextScheduledAt()
            )
        }
    }

    protected fun scheduleNotificationAt(timestamp: Long) {
        notificationManager.scheduleNotification(id, timestamp)
    }

    protected fun showNotification(id: Long, notification: Notification) {
        notificationManager.showNotification(id, notification)
    }
}