package org.hyperskill.app.android.notification.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.notification.remote.domain.interactor.PushNotificationsInteractor

class HyperskillFcmService : FirebaseMessagingService() {

    private var pushNotificationInteractor: PushNotificationsInteractor? = null

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        pushNotificationInteractor =
            HyperskillApp.graph().buildPushNotificationsComponent().pushNotificationsInteractor
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("HyperskillFcmService", "messageReceived: $message")
        val pushNotificationData = pushNotificationInteractor?.parsePushNotificationData(message.data)
        Log.d("HyperskillFcmService", "pushNotificationData: $pushNotificationData")
    }

    override fun onNewToken(token: String) {
        coroutineScope.launch {
            pushNotificationInteractor?.handleNewFCMToken(fcmToken = token)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}