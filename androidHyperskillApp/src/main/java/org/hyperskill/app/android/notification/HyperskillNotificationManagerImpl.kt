package org.hyperskill.app.android.notification

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import org.hyperskill.app.android.core.extensions.DateTimeHelper
import org.hyperskill.app.android.notification.receiver.AlarmReceiver
import org.hyperskill.app.notification.domain.interactor.NotificationInteractor
import ru.nobird.android.view.base.ui.extension.scheduleCompat

class HyperskillNotificationManagerImpl(
    private val context: Context,
    private val notificationInteractor: NotificationInteractor
) : HyperskillNotificationManager {

    private val alarmManager: AlarmManager by lazy { context.getSystemService(Context.ALARM_SERVICE) as AlarmManager }
    private val notificationManager: NotificationManager by lazy { context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    override fun scheduleNotification(id: String, millis: Long) {
        val intent = AlarmReceiver
            .createIntent(context, id)

        val pendingIntent = PendingIntent
            .getBroadcast(context, AlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)
        alarmManager.scheduleCompat(millis, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent)

        notificationInteractor.setNotificationTimestamp(id, millis)
    }

    override fun rescheduleActiveNotification(id: String, nextMillis: Long?) {
        val millis = notificationInteractor.getNotificationTimestamp(id)
        val nowUtc = DateTimeHelper.nowUtc()
        when {
            // saved schedule time is after now
            millis > 0L && millis > nowUtc -> scheduleNotification(id, millis)
            // saved schedule time is before now;
            // use next millis is it is after now
            nextMillis != null && nextMillis > 0 && nextMillis > nowUtc -> scheduleNotification(id, nextMillis)
            else -> {
                // no op
            }
        }
    }

    override fun showNotification(id: Long, notification: Notification) {
        notificationManager.notify(id.toInt(), notification)
    }
}