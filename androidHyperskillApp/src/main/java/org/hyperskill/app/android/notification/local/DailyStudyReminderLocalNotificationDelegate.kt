package org.hyperskill.app.android.notification.local

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.android.core.extensions.DateTimeHelper
import org.hyperskill.app.android.notification.NotificationBuilder
import org.hyperskill.app.android.notification.NotificationIntentBuilder
import org.hyperskill.app.android.notification.model.DailyStudyReminderClickedData
import org.hyperskill.app.android.notification.model.HyperskillNotificationChannel
import org.hyperskill.app.android.notification.model.NotificationId
import org.hyperskill.app.notification.local.domain.analytic.NotificationDailyStudyReminderShownHyperskillAnalyticEvent
import org.hyperskill.app.notification.local.domain.interactor.NotificationInteractor

class DailyStudyReminderLocalNotificationDelegate(
    hyperskillNotificationManager: HyperskillNotificationManager,
    private val context: Context,
    private val notificationInteractor: NotificationInteractor,
    private val analyticInteractor: AnalyticInteractor
) : LocalNotificationDelegate(KEY, hyperskillNotificationManager) {
    companion object {
        const val KEY = "daily_study_reminder_notification"
    }

    override fun onNeedShowNotification() {
        if (!notificationInteractor.isDailyStudyRemindersEnabled()) {
            return
        }
        scheduleDailyNotification()

        val notificationDescription = notificationInteractor.getRandomDailyStudyRemindersNotificationDescription()

        val pendingIntent = with(NotificationIntentBuilder) {
            buildActivityPendingIntent(context) {
                addClickedNotificationDataExtra(
                    DailyStudyReminderClickedData(notificationId = notificationDescription.id)
                )
            }
        }

        val notification = NotificationBuilder.getSimpleNotificationBuilder(
            context = context,
            channel = HyperskillNotificationChannel.DailyReminder.channelId,
            title = notificationDescription.title,
            body = notificationDescription.text,
            pendingIntent = pendingIntent
        )

        showNotification(NotificationId.DailyStudyReminder.notificationId, notification.build())

        logShownNotificationEvent(notificationDescription.id)
    }

    override fun getNextScheduledAt(): Long? =
        getNextScheduledAtInternal()

    fun scheduleDailyNotification() {
        getNextScheduledAtInternal()?.let { scheduledAt ->
            scheduleNotificationAt(scheduledAt)
        }
    }

    private fun getNextScheduledAtInternal(): Long? {
        if (!notificationInteractor.isDailyStudyRemindersEnabled()) {
            return null
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

        return nextNotificationMillis
    }

    private fun logShownNotificationEvent(notificationId: Int) {
        val event = NotificationDailyStudyReminderShownHyperskillAnalyticEvent(
            route = HyperskillAnalyticRoute.Home(),
            notificationId = notificationId,
            plannedAtISO8601 = SimpleDateFormat(DateTimeHelper.ISO_PATTERN).format(Calendar.getInstance().time)
        )

        analyticInteractor.reportEvent(event)
    }
}