package org.hyperskill.app.notification.local.domain.interactor

import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.hyperskill.app.core.domain.repository.awaitNextFreshState
import org.hyperskill.app.notification.local.data.model.NotificationDescription
import org.hyperskill.app.notification.local.domain.flow.DailyStudyRemindersEnabledFlow
import org.hyperskill.app.notification.local.domain.repository.NotificationRepository
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.profile.domain.repository.ProfileRepository
import org.hyperskill.app.step_quiz.domain.repository.SubmissionRepository

class NotificationInteractor(
    private val notificationRepository: NotificationRepository,
    private val submissionRepository: SubmissionRepository,
    private val dailyStudyRemindersEnabledFlow: DailyStudyRemindersEnabledFlow,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val profileRepository: ProfileRepository
) {
    companion object {
        private val TWO_DAYS_IN_MILLIS = 2.toDuration(DurationUnit.DAYS).inWholeMilliseconds
        private const val MAX_USER_ASKED_TO_ENABLE_DAILY_REMINDERS_COUNT = 3
        private const val UTC_TIME_ZONE_ID = "UTC"
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

    fun updateLastTimeUserAskedToEnableDailyReminders() {
        notificationRepository.setLastTimeUserAskedToEnableDailyReminders(
            Clock.System.now().toEpochMilliseconds()
        )
    }

    private fun getUserAskedToEnableDailyRemindersCount(): Long =
        notificationRepository.getUserAskedToEnableDailyRemindersCount()

    /**
     * Sets the daily study reminder notification time & set current timeZone
     *
     * @param notificationHour the hour of the day in 24-hour format (0-23) at which the notification should be shown.
     */
    internal suspend fun setDailyStudyReminderNotificationTime(notificationHour: Int): Result<Unit> =
        setDailyStudyReminderNotificationHourInternal(notificationHour)

    suspend fun setSavedDailyStudyReminderNotificationTime(): Result<Unit> =
        setDailyStudyReminderNotificationHourInternal(getDailyStudyRemindersIntervalStartHour())

    /**
     * Updates timezone to keep user notification time up to date
     */
    internal suspend fun updateTimeZone(): Result<Unit> =
        runCatching {
            val profile = currentProfileStateRepository.awaitNextFreshState()
            when {
                profile.notificationHour == null -> {
                    if (isDailyStudyRemindersEnabled()) {
                        setDailyStudyReminderNotificationHourInternal(getDailyStudyRemindersIntervalStartHour())
                    }
                }
                // If timezone is not set or the default UTC timezone is set,
                // then convert notificationHour from UTC to local timezone
                profile.timeZone == null || profile.timeZone.id == UTC_TIME_ZONE_ID -> {
                    val currentTimeZoneNotificationHour = convertFromUtcToCurrentTimeZone(profile.notificationHour)
                    setDailyStudyReminderNotificationHourInternal(currentTimeZoneNotificationHour)
                }
                else -> {
                    updateTimeZone(profile.id)
                }
            }
        }

    private fun convertFromUtcToCurrentTimeZone(utcNotificationHour: Int): Int {
        val currentTimeZone = TimeZone.currentSystemDefault()
        val utcTimeZone = TimeZone.UTC
        val utcNotificationTime =
            Clock.System.now()
                .toLocalDateTime(utcTimeZone)
                .date
                .atTime(hour = utcNotificationHour, minute = 0)
                .toInstant(utcTimeZone)
        val currentTimeZoneNotificationTime = utcNotificationTime.toLocalDateTime(currentTimeZone)
        return currentTimeZoneNotificationTime.hour
    }

    private suspend fun setDailyStudyReminderNotificationHourInternal(notificationHour: Int): Result<Unit> =
        runCatching {
            notificationRepository.setDailyStudyRemindersIntervalStartHour(notificationHour)
            val profileId = getProfileId().getOrThrow()
            val newProfile = profileRepository.setDailyStudyReminderNotificationHour(
                profileId = profileId,
                notificationHour = notificationHour,
                timeZone = TimeZone.currentSystemDefault()
            ).getOrThrow()
            currentProfileStateRepository.updateState(newProfile)
        }

    private suspend fun updateTimeZone(profileId: Long? = null) {
        val nonNullProfileId = profileId ?: getProfileId().getOrThrow()
        val newProfile = profileRepository.setTimeZone(
            profileId = nonNullProfileId,
            timeZone = TimeZone.currentSystemDefault()
        ).getOrThrow()
        currentProfileStateRepository.updateState(newProfile)
    }

    internal suspend fun disableDailyStudyReminderNotification(): Result<Unit> =
        runCatching {
            val profileId = getProfileId().getOrThrow()
            val newProfile = profileRepository.disableDailyStudyReminderNotification(profileId).getOrThrow()
            currentProfileStateRepository.updateState(newProfile)
        }

    private suspend fun getProfileId(): Result<Long> =
        currentProfileStateRepository.getState(forceUpdate = false).map { it.id }
}