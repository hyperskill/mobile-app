package org.hyperskill.app.android.notification.remote

import android.content.Context
import org.hyperskill.app.android.notification.remote.model.PushNotification
import org.hyperskill.app.notification.remote.domain.model.PushNotificationData

interface PushNotificationHandler {
    suspend fun onNotificationReceived(context: Context, notification: PushNotification?, data: PushNotificationData?)
}