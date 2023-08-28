package org.hyperskill.app.android.notification.local

import android.content.Context
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlinx.coroutines.runBlocking
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
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository

class DailyStudyReminderLocalNotificationDelegate(
    hyperskillNotificationManager: HyperskillNotificationManager,
    private val context: Context,
    private val notificationInteractor: NotificationInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val currentProfileStateRepository: CurrentProfileStateRepository
) : LocalNotificationDelegate(KEY, hyperskillNotificationManager) {
    companion object {
        const val KEY = "daily_study_reminder_notification"
        private const val LOG_TAG = "DailyStudyReminderLocalNotificationDelegate"
    }

    override fun onNeedShowNotification() {
        Log.i(LOG_TAG, "onNeedShowNotification has called")
        if (wasNotificationTimeSentToServer() || !notificationInteractor.isDailyStudyRemindersEnabled()) {
            return
        }

        val notificationDescription = notificationInteractor.getRandomDailyStudyRemindersNotificationDescription()

        val notificationId = NotificationId.DailyStudyReminder.notificationId
        val pendingIntent = with(NotificationIntentBuilder) {
            buildActivityPendingIntent(context, notificationId.toInt()) {
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

        sendNotificationTimeToServer()
        logShownNotificationEvent(notificationDescription.id)
    }

    override fun getNextScheduledAt(): Long? {
        val time = if (wasNotificationTimeSentToServer() || !notificationInteractor.isDailyStudyRemindersEnabled()) {
            null
        } else {
            getNextScheduledAtInternal(notificationInteractor.getDailyStudyRemindersIntervalStartHour())
        }
        Log.i(LOG_TAG, "getNextScheduledAt returns $time")
        return time
    }

    private fun wasNotificationTimeSentToServer(): Boolean =
        runBlocking {
            currentProfileStateRepository.getState(forceUpdate = false)
                .getOrNull()?.isDailyLearningNotificationEnabled ?: false
        }

    private fun sendNotificationTimeToServer() {
        runBlocking {
            notificationInteractor.setSavedDailyStudyReminderNotificationTime()
            Log.i(LOG_TAG, "Selected notification time successfully sent to the sever")
        }
    }

    private fun getNextScheduledAtInternal(hour: Int): Long {
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