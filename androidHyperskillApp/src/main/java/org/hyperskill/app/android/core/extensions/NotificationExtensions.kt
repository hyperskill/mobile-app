package org.hyperskill.app.android.core.extensions

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import org.hyperskill.app.android.notification.model.HyperskillNotificationChannel

fun NotificationChannel.isFullyEnabled(notificationManager: NotificationManagerCompat): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        if (importance == NotificationManager.IMPORTANCE_NONE) {
            return false
        }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        if (notificationManager.getNotificationChannelGroup(group)?.isBlocked == true) {
            return false
        }
    }

    return true
}

fun NotificationManagerCompat.isChannelNotificationsEnabled(channelId: String): Boolean =
    areNotificationsEnabled() && getNotificationChannel(channelId)?.isFullyEnabled(this) == true

fun NotificationManagerCompat.checkNotificationChannelAvailability(
    context: Context,
    notificationChannel: HyperskillNotificationChannel,
    onError: () -> Unit
): Boolean {
    fun startActivitySafe(intent: Intent) {
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            onError()
        }
    }
    return when {
        !areNotificationsEnabled() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                startActivitySafe(intent)
            }
            false
        }
        !isChannelNotificationsEnabled(notificationChannel.channelId) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    .putExtra(Settings.EXTRA_CHANNEL_ID, notificationChannel.channelId)
                startActivitySafe(intent)
            }
            false
        }
        else -> true
    }
}