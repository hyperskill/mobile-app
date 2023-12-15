package org.hyperskill.app.android.notification.local

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import org.hyperskill.app.android.core.extensions.DateTimeHelper
import org.hyperskill.app.android.notification.PendingIntentCompat
import org.hyperskill.app.android.notification.local.receiver.AlarmReceiver
import org.hyperskill.app.notification.local.domain.interactor.NotificationInteractor

class HyperskillNotificationManagerImpl(
    private val context: Context,
    private val notificationInteractor: NotificationInteractor
) : HyperskillNotificationManager {

    private val alarmManager: AlarmManager by lazy { context.getSystemService(Context.ALARM_SERVICE) as AlarmManager }
    private val notificationManager: NotificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun scheduleNotification(id: String, millis: Long) {
        val intent = AlarmReceiver
            .createIntent(context, id)

        val pendingIntent = PendingIntentCompat.getBroadcast(
            context,
            AlarmReceiver.REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
        scheduleCompat(alarmManager, millis, pendingIntent)

        notificationInteractor.setNotificationTimestamp(id, millis)
    }

    override fun rescheduleActiveNotification(id: String, nextMillis: Long?) {
        val millis = notificationInteractor.getNotificationTimestamp(id)
        val nowUtc = DateTimeHelper.nowUtc()
        when {
            // saved schedule time is after now
            millis > 0L && millis > nowUtc -> scheduleNotification(id, millis)
            // Saved schedule time is before now. Use next millis.
            nextMillis != null && nextMillis > 0 && nextMillis > nowUtc -> scheduleNotification(id, nextMillis)
            else -> {
                // no op
            }
        }
    }

    override fun showNotification(id: Long, notification: Notification) {
        notificationManager.notify(id.toInt(), notification)
    }

    private fun scheduleCompat(
        alarmManager: AlarmManager,
        scheduleMillis: Long,
        pendingIntent: PendingIntent
    ) {
        val triggerAtMillis = scheduleMillis + AlarmManager.INTERVAL_FIFTEEN_MINUTES / 2
        // Apps targeting sdk 31 or higher can schedule exact alarms
        // only if they have the Manifest.permission.SCHEDULE_EXACT_ALARM permission
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExactAndAllowWhileIdle(
                /* type = */ AlarmManager.RTC_WAKEUP,
                /* triggerAtMillis = */ triggerAtMillis,
                /* operation = */ pendingIntent
            )
        } else {
            // Schedule notification for non-exact time on Android 13 and above
            alarmManager.setAndAllowWhileIdle(
                /* type = */ AlarmManager.RTC_WAKEUP,
                /* triggerAtMillis = */ triggerAtMillis,
                /* operation = */ pendingIntent
            )
        }
    }
}