package org.hyperskill.app.push_notifications.domain

import android.util.Log
import org.hyperskill.app.play_services.domain.PlayServicesChecker
import org.hyperskill.app.push_notifications.domain.interactor.PushNotificationsInteractor

class PushNotificationDeviceRegistrar(
    private val fcmTokenProvider: FCMTokenProvider,
    private val pushNotificationsInteractor: PushNotificationsInteractor,
    private val playServicesChecker: PlayServicesChecker
) {
    suspend fun registerDeviceToPushes() {
        if (playServicesChecker.arePlayServicesAvailable()) {
            val token = try {
                fcmTokenProvider.getToken()
            } catch (e: Exception) {
                Log.w("MainViewModel", "Fetching FCM registration token failed", e)
                return
            }
            pushNotificationsInteractor.handleNewFCMToken(token)
        }
    }
}