package org.hyperskill.app.android.notification

import android.content.Context
import androidx.core.app.NotificationCompat
import java.util.Calendar
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.DateTimeHelper
import org.hyperskill.app.android.notification.model.ClickedNotificationData
import org.hyperskill.app.android.notification.model.HyperskillNotificationChannel
import org.hyperskill.app.notification.domain.interactor.NotificationInteractor

class DailyStudyReminderNotificationDelegate(
    hyperskillNotificationManager: HyperskillNotificationManager,
    private val context: Context,
    private val notificationInteractor: NotificationInteractor
) : NotificationDelegate(KEY, hyperskillNotificationManager) {
    companion object {
        const val KEY = "daily_study_reminder_notification"
        private const val NotificationId: Long = 0
    }

    override fun onNeedShowNotification() {
        if (!notificationInteractor.isDailyStudyRemindersEnabled()) {
            return
        }
        scheduleDailyNotification()

        val pendingIntent = with(NotificationIntentBuilder) {
            buildActivityPendingIntent(context) {
                addClickedNotificationDataExtra(
                    ClickedNotificationData(
                        notificationId = NotificationId,
                        notificationChannel = HyperskillNotificationChannel.DailyReminder
                    )
                )
            }
        }

        val notificationDescription = notificationInteractor.getRandomDailyStudyRemindersNotificationDescription()

        val notification = NotificationCompat.Builder(context, HyperskillNotificationChannel.DailyReminder.channelId)
            .setContentTitle(notificationDescription.title)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(notificationDescription.text)
            )
            .setSmallIcon(R.drawable.ic_branded_logo_splash)
            .setContentIntent(pendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        showNotification(NotificationId, notification.build())
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