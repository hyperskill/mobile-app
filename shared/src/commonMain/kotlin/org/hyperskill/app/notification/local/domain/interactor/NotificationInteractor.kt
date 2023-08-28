package org.hyperskill.app.notification.local.domain.interactor

import kotlin.time.Duration.Companion.hours
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.hyperskill.app.notification.local.data.model.NotificationDescription
import org.hyperskill.app.notification.local.domain.flow.DailyStudyRemindersEnabledFlow
import org.hyperskill.app.notification.local.domain.repository.NotificationRepository
import org.hyperskill.app.notification.remote.domain.repository.NotificationTimeRepository
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.step_quiz.domain.repository.SubmissionRepository

class NotificationInteractor(
    private val notificationRepository: NotificationRepository,
    private val submissionRepository: SubmissionRepository,
    private val dailyStudyRemindersEnabledFlow: DailyStudyRemindersEnabledFlow,
    private val notificationTimeRepository: NotificationTimeRepository,
    private val currentProfileStateRepository: CurrentProfileStateRepository
) {
    companion object {
        private val TWO_DAYS_IN_MILLIS = 2.toDuration(DurationUnit.DAYS).inWholeMilliseconds
        private const val MAX_USER_ASKED_TO_ENABLE_DAILY_REMINDERS_COUNT = 3
    }

    val solvedStepsSharedFlow: SharedFlow<Long> = submissionRepository.solvedStepsMutableSharedFlow

    fun isNotificationsPermissionGranted(): Boolean =
        notificationRepository.isNotificationsPermissionGranted()

    fun setNotificationsPermissionGranted(isGranted: Boolean) {
        notificationRepository.setNotificationsPermissionGranted(isGranted)
    }

    fun isDailyStudyRemindersEnabled(): Boolean =
        notificationRepository.isDailyStudyRemindersEnabled()

    suspend fun setDailyStudyRemindersEnabled(enabled: Boolean) {
        notificationRepository.setDailyStudyRemindersEnabled(enabled)

        dailyStudyRemindersEnabledFlow.notifyDataChanged(enabled)
    }

    fun getNotificationTimestamp(key: String): Long =
        notificationRepository.getNotificationTimestamp(key)

    fun setNotificationTimestamp(key: String, timestamp: Long) {
        notificationRepository.setNotificationTimestamp(key, timestamp)
    }

    fun getDailyStudyRemindersIntervalStartHour(): Int =
        notificationRepository.getDailyStudyRemindersIntervalStartHour()

    fun getRandomDailyStudyRemindersNotificationDescription(): NotificationDescription =
        notificationRepository.getRandomDailyStudyRemindersNotificationDescription()

    fun isRequiredToAskUserToEnableDailyReminders(): Boolean {
        if (notificationRepository.isDailyStudyRemindersEnabled()) {
            return false
        }

        if (submissionRepository.getSolvedStepsCount() <= 1L) {
            return true
        }

        val lastTimeAsked = notificationRepository.getLastTimeUserAskedToEnableDailyReminders() ?: return true
        val isTwoDaysPassed = (lastTimeAsked + TWO_DAYS_IN_MILLIS) <= Clock.System.now().toEpochMilliseconds()

        val isNotReachedMaxUserAskedCount =
            getUserAskedToEnableDailyRemindersCount() < MAX_USER_ASKED_TO_ENABLE_DAILY_REMINDERS_COUNT

        return isTwoDaysPassed && isNotReachedMaxUserAskedCount
    }

    fun setLastTimeUserAskedToEnableDailyReminders(timestamp: Long) {
        notificationRepository.setLastTimeUserAskedToEnableDailyReminders(timestamp)
    }

    private fun getUserAskedToEnableDailyRemindersCount(): Long =
        notificationRepository.getUserAskedToEnableDailyRemindersCount()

    /**
     * Sets the daily study reminder notification time.
     *
     * @param notificationHour the hour of the day in 24-hour format (0-23) at which the notification should be shown.
     */
    internal suspend fun setDailyStudyReminderNotificationTime(notificationHour: Int): Result<Unit> {
        notificationRepository.setDailyStudyRemindersIntervalStartHour(notificationHour)
        val utcNotificationHour = getUtcDailyStudyReminderNotificationHour(notificationHour)
        return notificationTimeRepository
            .setDailyStudyReminderNotificationTime(notificationHour = utcNotificationHour)
    }

    internal suspend fun setSavedDailyStudyReminderNotificationTime(): Result<Unit> =
        setDailyStudyReminderNotificationTime(getDailyStudyRemindersIntervalStartHour())

    /**
     * Updates notification time on the server-side to keep the user timezone up to date
     */
    internal suspend fun updateDailyStudyReminderNotificationTime(): Result<Unit> {
        // Wait for the first emitted value, to get the fresh value without an additional network call
        val notificationHour = currentProfileStateRepository
            .changes
            .first()
            .dailyLearningNotificationHour
        return when {
            notificationHour != null -> {
                setDailyStudyReminderNotificationTime(notificationHour)
            }
            isDailyStudyRemindersEnabled() -> {
                setSavedDailyStudyReminderNotificationTime()
            }

            else -> {
                Result.success(Unit)
            }
        }
    }

    private fun getUtcDailyStudyReminderNotificationHour(notificationHour: Int): Int {
        val currentTimeZone = TimeZone.currentSystemDefault()
        val currentTimeZoneNotificationTime =
            Clock.System.now()
                .toLocalDateTime(currentTimeZone)
                .date
                .atTime(hour = notificationHour, minute = 0)
                .toInstant(currentTimeZone)
        val utcNotificationTime = currentTimeZoneNotificationTime.toLocalDateTime(TimeZone.UTC)
        return if (utcNotificationTime.minute > 0) {
            // In case the time zone contains minutes, add 1 hour
            // to have notification hour at the beginning of the next hour
            currentTimeZoneNotificationTime
                .plus(1.hours)
                .toLocalDateTime(TimeZone.UTC)
                .hour
        } else {
            utcNotificationTime.hour
        }
    }

    internal suspend fun disableDailyStudyReminderNotification(): Result<Unit> =
        notificationTimeRepository.disableDailyStudyReminderNotification()
}