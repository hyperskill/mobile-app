package org.hyperskill.app.android.notification.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class HyperskillFcmService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("HyperskillFcmService", "messageReceived")
    }

    override fun onNewToken(token: String) {
        // TODO: Implement token refresh
    }
}