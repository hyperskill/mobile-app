package org.hyperskill.app.notification.local.domain.interactor

import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.datetime.Clock
import org.hyperskill.app.notification.local.data.model.NotificationDescription
import org.hyperskill.app.notification.local.domain.flow.DailyStudyRemindersEnabledFlow
import org.hyperskill.app.notification.local.domain.repository.NotificationRepository
import org.hyperskill.app.step_quiz.domain.repository.SubmissionRepository

class NotificationInteractor(
    private val notificationRepository: NotificationRepository,
    private val submissionRepository: SubmissionRepository,
    private val dailyStudyRemindersEnabledFlow: DailyStudyRemindersEnabledFlow
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

    fun setDailyStudyRemindersIntervalStartHour(hour: Int) {
        notificationRepository.setDailyStudyRemindersIntervalStartHour(hour)
    }

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

    fun getShuffledDailyStudyRemindersNotificationDescriptions(): List<NotificationDescription> =
        notificationRepository.getShuffledDailyStudyRemindersNotificationDescriptions()
}