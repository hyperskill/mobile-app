package org.hyperskill.app.android.notification.model

import android.app.NotificationManager
import androidx.annotation.StringRes
import org.hyperskill.app.android.R

private fun getImportanceCompat(): Int =
    NotificationManager.IMPORTANCE_HIGH

private const val dailyReminderId = "dailyReminderChannel"
private const val streakBoostersId = "streakBoostersChannel"
private const val streakSaversId = "streakSaversChannel"
private const val regularLearningRemindersId = "regularLearningRemindersChannel"
private const val otherId = "otherChannel"

enum class HyperskillNotificationChannel(
    val channelId: String,
    @StringRes val visibleChannelNameRes: Int,
    @StringRes val visibleChannelDescriptionRes: Int? = null,
    val importance: Int = getImportanceCompat()
) {
    // order is important!

    DailyReminder(dailyReminderId, R.string.daily_reminder_channel_name, R.string.daily_reminder_channel_description),
    RegularLearningReminders(
        regularLearningRemindersId,
        R.string.regular_learning_reminders_channel_name,
        R.string.regular_learning_reminders_channel_description
    ),
    StreakBoosters(
        streakBoostersId,
        R.string.streak_boosters_channel_name,
        R.string.streak_boosters_channel_description
    ),
    StreakSavers(streakSaversId, R.string.streak_savers_channel_name, R.string.streak_savers_channel_description),

    Other(otherId, R.string.other_channel_name)
}