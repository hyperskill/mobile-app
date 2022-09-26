package org.hyperskill.app.android.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.DateTimeHelper
import org.hyperskill.app.android.main.view.ui.activity.MainActivity
import org.hyperskill.app.android.notification.model.HyperskillNotificationChannel
import org.hyperskill.app.notification.domain.interactor.NotificationInteractor
import java.util.Calendar

class DailyStudyReminderNotificationDelegate(
    private val context: Context,
    private val hyperskillNotificationManager: HyperskillNotificationManager,
    private val notificationInteractor: NotificationInteractor
) : NotificationDelegate(KEY, hyperskillNotificationManager) {
    companion object {
        const val KEY = "daily_study_reminder_notification"
    }

    override fun onNeedShowNotification() {
        if (!notificationInteractor.isDailyStudyRemindersEnabled()) {
            return
        }
        scheduleDailyNotification()

        val intent = Intent(HyperskillApp.getAppContext(), MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(HyperskillApp.getAppContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationDescription = notificationInteractor.getRandomDailyStudyRemindersNotificationDescription()

        val notification = NotificationCompat.Builder(context, HyperskillNotificationChannel.DAILY_REMINDER.channelId)
            .setContentTitle(notificationDescription.title)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(notificationDescription.text)
            )
            .setSmallIcon(R.drawable.ic_branded_logo_splash)
            .setContentIntent(pendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        showNotification(0, notification.build())
    }

    fun scheduleDailyNotification() {
        if (!notificationInteractor.isDailyStudyRemindersEnabled()) {
            return
        }

        val hour = notificationInteractor.getDailyStudyRemindersIntervalStartHour()
        val now = DateTimeHelper.nowUtc()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        var nextNotificationMillis = calendar.timeInMillis

        if (nextNotificationMillis < now) {
            nextNotificationMillis += DateTimeHelper.MILLIS_IN_24HOURS
        }

        scheduleNotificationAt(nextNotificationMillis)
    }
}