package org.hyperskill.app.notification.domain

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.datetime.Clock
import org.hyperskill.app.extension.AppConstants
import org.hyperskill.app.notification.data.model.NotificationDescription
import org.hyperskill.app.notification.domain.repository.NotificationRepository
import org.hyperskill.app.step_quiz.domain.repository.SubmissionRepository

class NotificationInteractor(
    private val notificationRepository: NotificationRepository,
    private val submissionRepository: SubmissionRepository
) {
    val solvedStepsSharedFlow: SharedFlow<Long> = submissionRepository.solvedStepsMutableSharedFlow

    fun isDailyStudyRemindersEnabled(): Boolean =
        notificationRepository.isDailyStudyRemindersEnabled()

    fun setDailyStudyRemindersEnabled(enabled: Boolean) {
        notificationRepository.setDailyStudyRemindersEnabled(enabled)
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

        if (submissionRepository.getStepsInAppSolved() == 0L) {
            return true
        }

        val lastTimeAsked = notificationRepository.getLastTimeUserAskedToEnableDailyReminders() ?: return true

        return lastTimeAsked + AppConstants.TWO_DAYS_IN_MILLIS <= Clock.System.now().toEpochMilliseconds() && getUserAskedToEnableDailyRemindersCount() < 3
    }

    fun setLastTimeUserAskedToEnableDailyReminders(timestamp: Long) {
        notificationRepository.setLastTimeUserAskedToEnableDailyReminders(timestamp)
    }

    private fun getUserAskedToEnableDailyRemindersCount(): Long =
        notificationRepository.getUserAskedToEnableDailyRemindersCount()

    fun getShuffledDailyStudyRemindersNotificationDescriptions(): List<NotificationDescription> =
        notificationRepository.getShuffledDailyStudyRemindersNotificationDescriptions()
}