package org.hyperskill.app.android.notification.model

import android.app.NotificationManager
import androidx.annotation.StringRes
import org.hyperskill.app.android.R

private fun getImportanceCompat(): Int =
    NotificationManager.IMPORTANCE_HIGH

private const val dailyReminderId = "dailyReminderChannel"
private const val otherId = "otherChannel"

enum class HyperskillNotificationChannel(
    val channelId: String,
    @StringRes val visibleChannelNameRes: Int,
    @StringRes val visibleChannelDescriptionRes: Int,
    val importance: Int = getImportanceCompat()
) {
    // order is important!

    DailyReminder(dailyReminderId, R.string.daily_reminder_channel_name, R.string.daily_reminder_channel_name),
    Other(otherId, R.string.other_channel_name, R.string.other_channel_name),
}