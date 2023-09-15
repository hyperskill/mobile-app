package org.hyperskill.app.notification.remote.injection

import com.google.firebase.messaging.FirebaseMessaging
import org.hyperskill.app.notification.remote.data.AndroidFCMTokenRepository
import org.hyperskill.app.notification.remote.domain.repository.FCMTokenRepository
import org.hyperskill.app.play_services.injection.PlayServicesCheckerComponent

class AndroidPlatformPushNotificationsPlatformDataComponent(
    private val playServicesCheckerComponent: PlayServicesCheckerComponent
) : PlatformPushNotificationsDataComponent {

    override val fcmTokenRepository: FCMTokenRepository
        get() = AndroidFCMTokenRepository(
            firebaseMessaging = FirebaseMessaging.getInstance(),
            playServicesChecker = playServicesCheckerComponent.playServicesChecker
        )
}