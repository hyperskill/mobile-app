package org.hyperskill.app.android.notification.local

import android.app.Notification

/**
 * To make it works add permissions, services & receivers to the manifest
 *
 * <uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" />
 * <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
 * <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
 *
 * <uses-permission
 *         android:name="android.permission.WAKE_LOCK"
 *         android:maxSdkVersion="26"/>
 *
 * <receiver android:name=".notification.local.receiver.AlarmReceiver" />
 *
 * <service
 *     android:name=".notification.local.service.RescheduleLocalNotificationsService"
 *     android:exported="true"
 *     android:permission="android.permission.BIND_JOB_SERVICE" />
 *
 * <receiver
 *     android:name=".notification.local.receiver.RescheduleNotificationsReceiver"
 *     android:enabled="true"
 *     android:exported="true"
 *     android:permission="android.permission.RECEIVE_BOOT_COMPLETED">
 *     <intent-filter>
 *         <action android:name="android.intent.action.BOOT_COMPLETED" />
 *         <action android:name="android.intent.action.MY_PACKAGE_REPLACED" />
 *     </intent-filter>
 * </receiver>
 */
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