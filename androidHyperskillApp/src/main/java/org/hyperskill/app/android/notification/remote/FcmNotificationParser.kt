package org.hyperskill.app.android.notification.remote

import com.google.firebase.messaging.RemoteMessage
import kotlinx.serialization.json.Json
import org.hyperskill.app.android.notification.remote.model.PushNotification
import org.hyperskill.app.core.utils.toJsonElement

class FcmNotificationParser(
    private val json: Json
) {
    fun parseNotification(remoteMessage: RemoteMessage): PushNotification =
        json.decodeFromJsonElement(PushNotification.serializer(), remoteMessage.data.toJsonElement())
}