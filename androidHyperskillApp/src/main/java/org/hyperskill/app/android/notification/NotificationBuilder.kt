package org.hyperskill.app.android.notification

import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import org.hyperskill.app.android.R

object NotificationBuilder {
    fun getSimpleNotificationBuilder(
        context: Context,
        channel: String,
        title: String,
        body: String,
        pendingIntent: PendingIntent
    ): NotificationCompat.Builder =
        NotificationCompat.Builder(context, channel)
            .setContentTitle(title)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setSmallIcon(R.drawable.ic_notifiaction_small)
            .setColor(ContextCompat.getColor(context, org.hyperskill.app.R.color.color_primary))
            .setContentIntent(pendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
}