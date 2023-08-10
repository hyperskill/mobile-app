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
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        areNotificationsEnabled() && getNotificationChannel(channelId)?.isFullyEnabled(this) == true
    } else {
        areNotificationsEnabled()
    }

fun NotificationManagerCompat.checkNotificationChannelAvailability(
    context: Context,
    notificationChannel: HyperskillNotificationChannel,
    onError: () -> Unit = {}
): Boolean =
    when {
        !areNotificationsEnabled() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startAppNotificationSettingsIntent(onError)
            }
            false
        }
        !isChannelNotificationsEnabled(notificationChannel.channelId) -> {
            context.startNotificationChannelSettingsIntent(notificationChannel, onError)
            false
        }
        else -> true
    }

fun Context.startAppNotificationSettingsIntent(onError: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        startActivitySafe(this, intent, onError)
    }
}

fun Context.startNotificationChannelSettingsIntent(
    notificationChannel: HyperskillNotificationChannel,
    onError: () -> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
            .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            .putExtra(Settings.EXTRA_CHANNEL_ID, notificationChannel.channelId)
        startActivitySafe(this, intent, onError)
    }
}

private fun startActivitySafe(context: Context, intent: Intent, onError: () -> Unit) {
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        onError()
    }
}