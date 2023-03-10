package org.hyperskill.app.notification.cache

internal object NotificationCacheKeyValues {
    const val NOTIFICATIONS_PERMISSION_GRANTED = "notifications_permission_granted"

    const val DAILY_STUDY_REMINDERS_ENABLED = "notifications_daily_reminder_enabled"
    const val DAILY_STUDY_REMINDERS_START_HOUR = "notifications_daily_reminder_start_hour"
    const val DAILY_STUDY_REMINDERS_START_HOUR_DEFAULT = 20
    const val DAILY_STUDY_REMINDERS_START_HOUR_AFTER_STEP_SOLVED = 12
    const val DAILY_STUDY_REMINDERS_LAST_TIME_USER_ASKED_TO_ENABLE =
        "notifications_last_time_user_asked_to_enable_daily_reminders"
    const val DAILY_STUDY_REMINDERS_USER_ASKED_TO_ENABLE_COUNT =
        "notifications_user_asked_to_enable_daily_reminders_count"
}