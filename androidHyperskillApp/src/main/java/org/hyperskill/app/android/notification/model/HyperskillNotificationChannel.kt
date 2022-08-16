package org.hyperskill.app.android.notification.model

import android.app.NotificationManager
import androidx.annotation.StringRes
import org.hyperskill.app.android.R

private fun getImportanceCompat(): Int =
    NotificationManager.IMPORTANCE_HIGH

private val dailyReminderId = "dailyReminderChannel"
private val otherId = "otherChannel"

enum class HyperskillNotificationChannel(
    val channelId: String,
    @StringRes
    val visibleChannelNameRes: Int,
    @StringRes
    val visibleChannelDescriptionRes: Int,
    val importance: Int = getImportanceCompat()
) {
    // order is important!

    DAILY_REMINDER(dailyReminderId, R.string.daily_reminder_channel_name, R.string.daily_reminder_channel_name),
    other(otherId, R.string.other_channel_name, R.string.other_channel_name),
}
