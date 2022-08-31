package org.hyperskill.app.android.core.extensions

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat

fun NotificationChannel.isFullyEnabled(notificationManager: NotificationManagerCompat): Boolean {
    if (importance == NotificationManager.IMPORTANCE_NONE)
        return false

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        if (notificationManager.getNotificationChannelGroup(group)?.isBlocked == true)
            return false
    }

    return true
}