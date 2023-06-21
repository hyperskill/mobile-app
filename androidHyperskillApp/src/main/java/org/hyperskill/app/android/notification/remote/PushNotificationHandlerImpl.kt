package org.hyperskill.app.android.notification.remote

import android.content.Context
import android.os.Looper
import android.util.Log
import org.hyperskill.app.android.notification.NotificationBuilder
import org.hyperskill.app.android.notification.NotificationIntentBuilder
import org.hyperskill.app.android.notification.local.HyperskillNotificationManager
import org.hyperskill.app.android.notification.model.HyperskillNotificationChannel
import org.hyperskill.app.android.notification.model.PushNotificationClickedData
import org.hyperskill.app.android.notification.remote.model.PushNotification
import org.hyperskill.app.android.notification.remote.model.channel
import org.hyperskill.app.android.notification.remote.model.id
import org.hyperskill.app.notification.remote.domain.model.PushNotificationData

class PushNotificationHandlerImpl(
    private val notificationManager: HyperskillNotificationManager
) : PushNotificationHandler {
    override fun onNotificationReceived(
        context: Context,
        notification: PushNotification?,
        data: PushNotificationData?
    ) {
        Log.d("PushNotificationHandlerImpl", "\nnotification=$notification\ndata=$data")
        // TODO: add analytics
        if (notification != null) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                throw RuntimeException("Can't create notification on main thread")
            }
            val androidNotification = NotificationBuilder.getSimpleNotificationBuilder(
                context = context,
                channel = data?.channel?.channelId ?: HyperskillNotificationChannel.Other.channelId,
                title = notification.title,
                body = notification.body,
                pendingIntent = with(NotificationIntentBuilder) {
                    buildActivityPendingIntent(context) {
                        if (data != null) {
                            addClickedNotificationDataExtra(PushNotificationClickedData(data))
                        }
                    }
                }
            )
            notificationManager.showNotification(
                id = data?.id?.notificationId ?: 0,
                notification = androidNotification.build()
            )
        }
    }
}